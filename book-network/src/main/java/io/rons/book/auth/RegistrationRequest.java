package io.rons.book.auth;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter@Setter@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not well formatted.")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 16,message = "Password between min 8 to 16 max")
    private String password;


}
