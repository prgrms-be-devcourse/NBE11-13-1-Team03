package com.team3.coffee_order.exception;

import com.team3.coffee_order.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> menuNotFoundException(MenuNotFoundException e) {
        log.warn("404 응답(메뉴 없음): {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage())
                );
    }

    //* 최후의 보루 핸들러 : 위에서 처리하지 못한 나머지 "모든 예외"를 잡는다
    //예상 못한 예외에 대해서 안전망 역할 수행
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> orderNotFoundException(OrderNotFoundException e) {
        log.warn("404 응답(주문 없음) : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    // 요청 값/본문/타입 오류는 500 핸들러로 넘어가지 않도록 400 응답으로 분리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("요청 값의 형식이 올바르지 않습니다.");

        log.warn("400 응답(요청 값 검증 실패): {}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("400 응답(잘못된 요청 본문): {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "요청 본문이 올바르지 않습니다."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("400 응답(잘못된 타입): name={}, value={}", e.getName(), e.getValue());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                "요청 값의 타입이 올바르지 않습니다. " + e.getName() + "=" + e.getValue()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> exception(Exception e) {
        log.error("500 응답(예상치 못한 예외 발생)", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error."));
    //NotFoundException 처리
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),e.getMessage())
                );
    }
}
