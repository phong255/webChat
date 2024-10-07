package dev.phong.webChat.handler.exceptionHandler.exeption;

import dev.phong.webChat.common.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DaoException extends RuntimeException{

    private final ServiceError serviceError;

    public DaoException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceError.ServiceErrors.ERROR.getCode(), message);
    }

    public DaoException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
