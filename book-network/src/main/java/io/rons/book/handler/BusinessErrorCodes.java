package io.rons.book.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED,"No Code"),
    ACCOUNT_LOCKED(302,FORBIDDEN,"User Account is Locked"),
    INCORRECT_CURRENT_PASSWORD(300,BAD_REQUEST,"CUrrent Password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301,BAD_REQUEST,"New Password does not match"),
    ACCOUNT_DISABLED(303,FORBIDDEN,"User Account is Disabled"),
    BAD_CREDENTIALS(304,FORBIDDEN,"Login and/ or password is Wrong."),



    ;


    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


}
