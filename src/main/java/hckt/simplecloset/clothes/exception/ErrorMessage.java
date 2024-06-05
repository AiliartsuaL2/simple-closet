package hckt.simplecloset.clothes.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_EXIST_ID("ID가 존재하지 않아요"),
    NOT_EXIST_CATEGORY("카테고리가 존재하지 않아요"),
    NOT_EXIST_BRAND("브랜드가 존재하지 않아요"),
    NOT_EXIST_NAME("이름이 존재하지 않아요"),
    NOT_EXIST_CLOTHES("옷이 존재하지 않아요"),
    NOT_EXIST_MEMBER_ID("회원 ID가 존재하지 않아요"),
    PERMISSION_DENIED_TO_UPDATE("수정 권한이 없어요"),
    PERMISSION_DENIED_TO_DELETE("삭제 권한이 없어요");

    private final String message;
}