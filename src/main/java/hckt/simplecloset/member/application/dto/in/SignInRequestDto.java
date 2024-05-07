package hckt.simplecloset.member.application.dto.in;

import hckt.simplecloset.global.dto.ApplicationRequestDto;
import hckt.simplecloset.member.exception.ErrorMessage;

public record SignInRequestDto(String email, String password) implements ApplicationRequestDto {
    public SignInRequestDto {
        requiredArgumentValidation(email, ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        requiredArgumentValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }
}
