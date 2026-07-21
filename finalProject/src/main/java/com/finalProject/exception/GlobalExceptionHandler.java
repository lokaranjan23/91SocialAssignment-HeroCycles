package com.finalProject.exception;

import com.finalProject.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(InvalidDataEnteredException.class)
    public ResponseEntity<ErrorResponse> invalidDataEnteredException(InvalidDataEnteredException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "INVALID_DATA_ENTERED",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PartNotFoundException.class)
    public ResponseEntity<ErrorResponse> partNotFoundException(PartNotFoundException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "PART_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PartCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> partCategoryNotFoundException(PartCategoryNotFoundException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "PART_CATEGORY_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<ErrorResponse> variantNotFoundException(VariantNotFoundException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "VARIANT_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(DuplicatePartException.class)
    public ResponseEntity<ErrorResponse> duplicatePartException(DuplicatePartException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "PART_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(DuplicatePartCategoryException.class)
    public ResponseEntity<ErrorResponse> duplicatePartCategoryException(DuplicatePartCategoryException ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "DUPLICATE_CATEGORY_ENTERED",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }
}
