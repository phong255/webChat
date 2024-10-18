package dev.phong.webChat.util;

import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.handler.exceptionHandler.exeption.ValidationException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public final class ValidUtils {
    public static void throwErrors(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new ValidationException(
                    bindingResult.getFieldErrors()
                            .stream().map(e -> e.getField() + " " + e.getDefaultMessage())
                            .collect(Collectors.joining(" ,"))
            );
        }
    }
}
