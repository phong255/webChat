package dev.phong.webChat.handler.exceptionHandler.exeption;

import dev.phong.webChat.common.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException{

    private final ServiceError serviceError;

    public CustomException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceError.ServiceErrors.ERROR.getCode(), message);
    }

    public CustomException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
