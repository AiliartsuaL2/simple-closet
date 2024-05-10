package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.application.port.in.*;
import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.Token;
import hckt.simplecloset.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class JwtService implements CreateTokenUseCase, ExtractPayloadUseCase, RenewAccessTokenUseCase, ValidateTokenUseCase, GetAuthenticationUseCase {
    private final CreateTokenProvider createTokenProvider;
    private final GetTokenInfoProvider getTokenInfoProvider;
    private final UserDetailsService userDetailsService;
    private final LoadUserAccountPort loadUserAccountPort;

    @Override
    public Token create(Long userAccountId) {
        if (ObjectUtils.isEmpty(userAccountId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        if (loadUserAccountPort.findById(userAccountId).isEmpty()) {
            throw new IllegalStateException(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        String payload = String.valueOf(userAccountId);
        return new Token(createTokenProvider.createAccessToken(payload), createTokenProvider.createRefreshToken(payload));
    }

    @Override
    public Long extractPayload(String accessToken) {
        return Long.valueOf(getTokenInfoProvider.getPayload(accessToken));
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        String payload = getTokenInfoProvider.getPayload(refreshToken);
        return createTokenProvider.createAccessToken(payload);
    }

    @Override
    public boolean isValid(String token) {
        return getTokenInfoProvider.isValid(token);
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenInfoProvider.getPayload(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
