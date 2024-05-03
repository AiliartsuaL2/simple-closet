package hckt.simplecloset.member.application.port.in;

import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;

public interface SignUpUseCase {
    void signUp(SignUpRequestDto signUpUserInDto);
}
