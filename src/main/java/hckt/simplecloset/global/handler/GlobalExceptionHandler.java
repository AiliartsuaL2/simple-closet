package hckt.simplecloset.global.handler;

import hckt.simplecloset.global.dto.ApiErrorResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, StatusCode : {}, Message : {}";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> illegalArgumentException(IllegalArgumentException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> illegalStateException(IllegalStateException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> jwtException(JwtException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> runtimeException(RuntimeException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return writeLogTraceAndResponse(status, ex);
    }

    // 로그 트레이스를 남기고 응답을 한다
    private ResponseEntity<ApiErrorResponse> writeLogTraceAndResponse(HttpStatus httpStatus, Exception ex) {
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                httpStatus.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(httpStatus)
                .body(new ApiErrorResponse(ex.getMessage()));
    }
}
