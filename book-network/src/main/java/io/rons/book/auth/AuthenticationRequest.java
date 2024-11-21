package io.rons.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class AuthenticationRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not well formatted.")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 16,message = "Password between min 8 to 16 max")
    private String password;
}
