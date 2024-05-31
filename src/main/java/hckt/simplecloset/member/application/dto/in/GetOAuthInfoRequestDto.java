package hckt.simplecloset.member.application.dto.in;

import hckt.simplecloset.global.dto.ApplicationRequestDto;
import hckt.simplecloset.member.exception.ErrorMessage;

import java.util.List;

public record GetOAuthInfoRequestDto(String uid, String type) implements ApplicationRequestDto {
    private static final List<String> VALID_TYPE = List.of("sign-up", "sign-in");
    public GetOAuthInfoRequestDto {
        requiredArgumentValidation(uid, ErrorMessage.NOT_EXIST_UID.getMessage());
        requiredArgumentValidation(type, ErrorMessage.NOT_EXIST_TYPE.getMessage());
        if (!VALID_TYPE.contains(type)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TYPE.getMessage());
        }
    }
}