package hckt.simplecloset.member.application.dto.out;

import hckt.simplecloset.member.domain.OAuthInfo;

public record GetOAuthInfoResponseDto(String email, String image, String nickname, GetTokenResponseDto getTokenResponseDto) {
    public static GetOAuthInfoResponseDto fromEntity(OAuthInfo oAuthInfo, GetTokenResponseDto token) {
        return new GetOAuthInfoResponseDto(oAuthInfo.getEmail(), oAuthInfo.getImage(), oAuthInfo.getNickname(), token);
    }
}
