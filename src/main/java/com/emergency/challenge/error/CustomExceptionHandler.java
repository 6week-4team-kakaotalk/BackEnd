package com.emergency.challenge.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler{

//    서버 에러
//    @ExceptionHandler({ Exception.class })
//    protected ResponseEntity<Object> handleServerException(Exception ex) {
//        RestApiException error= new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(error);
//    }
    //바인드 에러
    @ExceptionHandler({BindException.class})
    protected ResponseEntity<Object> handleServerException(BindException ex) {
        RestApiException error = new RestApiException(ErrorCode.BIND_Fails.name(), ErrorCode.BIND_Fails.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
    //회원가입 정보 확인
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleApiRequestException(MethodArgumentNotValidException ex) {
        List<RestApiException> errors=new ArrayList<>();

        for( FieldError field : ex.getBindingResult().getFieldErrors()){
            errors.add(new RestApiException(field.getField(),field.getDefaultMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}