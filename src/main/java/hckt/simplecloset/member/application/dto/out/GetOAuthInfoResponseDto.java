package hckt.simplecloset.member.application.dto.out;

import hckt.simplecloset.member.domain.OAuthInfo;

public record GetOAuthInfoResponseDto(String email, String image, String nickname) {
    public static GetOAuthInfoResponseDto fromEntity(OAuthInfo oAuthInfo) {
        return new GetOAuthInfoResponseDto(oAuthInfo.getEmail(), oAuthInfo.getImage(), oAuthInfo.getNickname());
    }
}
