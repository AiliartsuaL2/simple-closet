package hckt.simplecloset.global.exception;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    NOT_EXIST_PROVIDER_TYPE("유효하지 않은 소셜 타입이에요");
    private final String message;
}
