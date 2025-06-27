package dev.lookforfps.oja.error.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiErrorException extends RuntimeException {

    private String message;
    private String type;
    private String parameter;
    private String code;

    public ApiErrorException(String message, String type, String parameter, String code) {
        super(message);
        this.message = message;
        this.type = type;
        this.parameter = parameter;
        this.code = code;
    }

}
