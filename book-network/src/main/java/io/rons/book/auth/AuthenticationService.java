package io.rons.book.auth;

import io.rons.book.email.EmailService;
import io.rons.book.email.EmailTemplateName;
import io.rons.book.role.RoleRepository;
import io.rons.book.security.JwtService;
import io.rons.book.user.Token;
import io.rons.book.user.TokenRepository;
import io.rons.book.user.User;
import io.rons.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistrationRequest registrationRequest) throws MessagingException {

        var userRole = roleRepository.findByName("USER")
//                TODO Better Excptional Handling.👍
                .orElseThrow(()->new IllegalStateException("ROle USER not Init."));

        log.info("FIrstname" + registrationRequest.getFirstname());
        log.info("Lastname" + registrationRequest.getLastname());


        var user = User.builder()
                .firstname(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);

        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
//        Send Email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );

//        tokenRepository.save(Token.builder().token(newToken).build());

    }

    private String generateAndSaveActivationToken(User user) {
        // Generate Token

        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int tokenLength) {

        String characters = "0123456789";

        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i =0;i<tokenLength;i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return  codeBuilder.toString();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword())
        );

        var claims = new HashMap<String,Object>();
        var user = ((User) auth.getPrincipal());

        claims.put("fullname",user.fullName());
        var jwtToken = jwtService.generateToken(claims,user);

        return AuthenticationResponse.builder()
                .token(jwtToken).build();


    }

//    Receive Token and acivate account for user.
//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("No token or Invalid Received"));

//        If expires generate new token
//        Getting an exception leads to rollback of the API. so remove the transactional annotation.
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation Token has Expired. New token has been sent to email");
        }

//        Update user with new token
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usernae not found due to token invalidation."));
        user.setEnabled(true);

        userRepository.save(user);
//      Update token Repo so that token in validated and audited sucessfully.
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
