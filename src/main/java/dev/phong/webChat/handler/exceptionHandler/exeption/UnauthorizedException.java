package dev.phong.webChat.handler.exceptionHandler.exeption;

import dev.phong.webChat.common.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedException extends RuntimeException{

    private final ServiceError serviceError;

    public UnauthorizedException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceError.ServiceErrors.UNAUTHORIZED.getCode(), message);
    }

    public UnauthorizedException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
