package hckt.simplecloset.member.application.dto.in;

import hckt.simplecloset.global.dto.ApplicationRequestDto;
import hckt.simplecloset.member.exception.ErrorMessage;

public record SignUpRequestDto(String email, String password, String picture, String provider) implements ApplicationRequestDto {
    public SignUpRequestDto {
        requiredArgumentValidation(email, ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        requiredArgumentValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        requiredArgumentValidation(provider, ErrorMessage.NOT_EXIST_PROVIDER.getMessage());
    }
}
