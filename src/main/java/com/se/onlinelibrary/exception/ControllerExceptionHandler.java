package com.se.onlinelibrary.exception;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
异常处理
 */
@Slf4j
@ControllerAdvice
@ResponseBody//表示返回的对象，Spring会自动把该对象进行json转化，最后写入到Response中。
public class ControllerExceptionHandler {
//    400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
//    403
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

//    406
    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<?> handleNotAcceptableException(NotAcceptableException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

//    202:已接受请求，但未处理完成
    @ExceptionHandler(CurrentUserHasRegistered.class)
    public ResponseEntity<?> handleCurrentUserHasRegistered(CurrentUserHasRegistered ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    //jwt token验证有关异常
    @ExceptionHandler(SignatureVerificationException.class)
    public Map<String, Object> handleSignatureVerificationException(SignatureVerificationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Map<String, Object> handleTokenExpiredException(TokenExpiredException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(AlgorithmMismatchException.class)
    public Map<String, Object> handleAlgorithmMismatchException(AlgorithmMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(JWTDecodeException.class)
    public Map<String, Object> handleJWTDecodeException(JWTDecodeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }
}
