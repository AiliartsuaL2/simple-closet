package hckt.simplecloset.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    NOT_EXIST_EMAIL("이메일이 존재하지 않아요"),
    NOT_EXIST_PASSWORD("비밀번호가 존재하지 않아요"),
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않아요");

    private final String message;
}