package hckt.simplecloset.member.application.port.in;

import hckt.simplecloset.member.application.dto.in.GetOAuthInfoRequestDto;
import hckt.simplecloset.member.application.dto.out.GetOAuthInfoResponseDto;

public interface GetOAuthInfoUseCase {
    GetOAuthInfoResponseDto getOAuthInfo(GetOAuthInfoRequestDto requestDto);
}
