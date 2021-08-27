package com.kkk.sbgtest.common;

import com.kkk.sbgtest.TestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = TestException.class)
//    public String handleBaseException(TestException e){
//        return e.getMessage();
//    }

//    @ExceptionHandler(value = Exception.class)
//    public String handleException(Exception e){
//        return e.getMessage();
//    }

    @ExceptionHandler({
            Throwable.class,
            Exception.class,
            TestException.class
    })
    public final ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
//        if (ex instanceof ApiServiceException) {
//            return handleApiProcessException((ApiServiceException) ex, headers, HttpStatus.OK, request);
//        } else {
//            return handleAll(ex, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
//        }
        return null;
    }

}


