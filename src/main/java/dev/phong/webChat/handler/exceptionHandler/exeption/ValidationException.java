package dev.phong.webChat.handler.exceptionHandler.exeption;

import dev.phong.webChat.common.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException{

    private final ServiceError serviceError;

    public ValidationException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceError.ServiceErrors.VALIDATION.getCode(), message);
    }

    public ValidationException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
