package hckt.simplecloset.global.exception;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_EXIST_MEMBER_ID("회원 ID가 존재하지 않아요"),
    NOT_EXIST_ROLE_TYPE("존재하지 않는 RoleType 이에요"),
    NOT_EXIST_USER_ACCOUNT("사용자 정보가 존재하지 않아요"),
    NOT_EXIST_MEMBER("존재하지 않는 회원이에요"),
    NOT_EXIST_PAYLOAD("payload가 존재하지 않아요"),
    NOT_EXIST_TOKEN("토큰이 존재하지 않아요"),
    INVALID_TOKEN("유효하지 않은 토큰이에요"),
    NOT_EXIST_PROVIDER_TYPE("유효하지 않은 소셜 타입이에요"),
    UNAUTHORIZED("해당 API에 대해 인증이 되지 않았어요"),
    PERMISSION_DENIED("해당 API에 대한 권한이 없어요"),
    SIGNATURE_TOKEN("서버의 토큰 형식과 일치하지 않아요"),
    MALFORMED_TOKEN("지원하지 않는 토큰 형식이에요"),
    EXPIRED_TOKEN("만료된 토큰 정보에요"),
    INCORRECT_TOKEN_TYPE_EXCEPTION("발급되지 않은 토큰 정보에요");

    private final String message;
}
