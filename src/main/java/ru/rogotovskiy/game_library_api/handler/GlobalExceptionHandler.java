package ru.rogotovskiy.game_library_api.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rogotovskiy.game_library_api.dto.ErrorResponse;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateObjectException;
import ru.rogotovskiy.game_library_api.exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(toErrorResponse(e));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateObjectException(DuplicateObjectException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(toErrorResponse(e));
    }

    private ErrorResponse toErrorResponse(Exception e) {
        return new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }
}
