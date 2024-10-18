package dev.phong.webChat.handler.exceptionHandler;

import dev.phong.webChat.common.ServiceError;
import dev.phong.webChat.common.ServiceResponse;
import dev.phong.webChat.handler.exceptionHandler.exeption.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    @Autowired
    HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse> handleException(Exception exception) {
        log.error(String.format("Exception: %s", exception.getMessage()), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.ERROR, exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {
        log.error(String.format("HttpMessageNotReadableException: %s", ex.getMessage()), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.BAD_REQUEST, ServiceError.ServiceErrors.BAD_REQUEST.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ServiceResponse> handleValidationCustomExceptionError(CustomException ex) {
        log.warn("CustomException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.ERROR, ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ServiceResponse> handleValidationExceptionError(ValidationException ex) {
        log.warn("ValidationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.VALIDATION, ex.getMessage()));
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ServiceResponse> handleValidationDaoExceptions(DaoException ex) {
        log.warn("DaoException: {}", ex.getServiceError().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.ERROR, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ServiceResponse> handleValidationUnauthorizedException(UnauthorizedException ex) {
        log.warn("UnauthorizedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServiceResponse> handleValidationUnauthorizedException(AccessDeniedException ex) {
        log.warn("AccessDeniedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ServiceResponse> handleBindException(ForbiddenException e) {
        log.warn("ForbiddenException: {}", e.getServiceError().getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ServiceResponse(new Date(), ServiceError.ServiceErrors.FORBIDDEN, ServiceError.ServiceErrors.FORBIDDEN.getMessage()));
    }
}