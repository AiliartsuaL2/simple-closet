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
    NOT_EXIST_CLOTHES("옷이 존재하지 않아요");

    private final String message;
}