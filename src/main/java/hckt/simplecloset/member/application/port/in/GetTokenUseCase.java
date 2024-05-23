package hckt.simplecloset.member.application.port.in;

import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;

public interface GetTokenUseCase {
    GetTokenResponseDto getToken(Long memberId);
}
