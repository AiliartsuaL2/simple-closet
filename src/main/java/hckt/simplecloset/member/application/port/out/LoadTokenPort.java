package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;

public interface LoadTokenPort {
    GetTokenResponseDto loadToken(Long memberId);
}
