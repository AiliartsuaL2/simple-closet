package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.domain.OAuthInfo;

import java.util.Optional;

public interface LoadOAuthInfoPort {
    OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto);
    Optional<OAuthInfo> loadOAuthInfo(String uid);
}
