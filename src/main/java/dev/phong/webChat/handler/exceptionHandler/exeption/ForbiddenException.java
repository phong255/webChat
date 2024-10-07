package dev.phong.webChat.handler.exceptionHandler.exeption;

import dev.phong.webChat.common.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForbiddenException extends RuntimeException{

    private final ServiceError serviceError;

    public ForbiddenException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceError.ServiceErrors.FORBIDDEN.getCode(), message);
    }

    public ForbiddenException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
