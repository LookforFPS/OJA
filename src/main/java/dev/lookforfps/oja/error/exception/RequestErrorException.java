package dev.lookforfps.oja.error.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class RequestErrorException extends RuntimeException {

    private Exception thrownException;

    public RequestErrorException(Exception thrownException) {
        super(thrownException.getMessage(), thrownException);
        this.thrownException = thrownException;
    }

}
