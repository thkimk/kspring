package com.kkk.sbgtest.model;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ApiResult {

    private String resultCode;

    private String message;

    private LinkedHashMap<String, Object> data;

    public ApiResult() { }

    /**
     * 생성자
     *
     * @param resultCode
     * @param message
     */
    public ApiResult(String resultCode, String message) {
        this.resultCode = resultCode;
        this.message 	= message;
    }

    /**
     * 생성자
     *
     * @param resultCode
     * @param message
     * @param data
     */
    public ApiResult(String resultCode, String message, LinkedHashMap<String, Object> data) {
        this.resultCode = resultCode;
        this.message 	= message;
        this.data 		= data;
    }


    public static ApiResult setSuccess() {
        return new ApiResult("200", "200 messages");
    }
/*
    public static ApiResult setResult(String code, String message) {
        return setResult(code, message, null, null);
    }
    public static ApiResult setResult(String code, DataMap data) {
        return setResult(code, getCodeMessage(code), data, null);
    }
    public static ApiResult setResult(String code, String message, DataMap data, Throwable throwable) {
        return new ApiResult(code, message, data);
    }
    public static ApiResult setSuccess(DataMap data) {
        return setResult(Codes.SUCCESS.getCode(), data);
    }
    public static ApiResult setSystemError() {
        return setResult(Codes.ERROR_9999.getCode());
    }
    public static ApiResult setSystemError(DataMap data) {
        return setResult(Codes.ERROR_9999.getCode(), data);
    }
    public static ResponseEntity<?> setHttpErrors(String code, HttpStatus httpStatus){
        return new ResponseEntity<>(setResult(code), new HttpHeaders(), httpStatus);
    }
*/
}
