package hckt.simplecloset.global.exception;

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
    NOT_EXIST_TOKEN("토큰이 존재하지 않아요");

    private final String message;
}
