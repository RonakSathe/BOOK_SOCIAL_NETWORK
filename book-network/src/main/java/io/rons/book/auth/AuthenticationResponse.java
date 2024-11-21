package io.rons.book.auth;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class AuthenticationResponse {

    private String token;

}
