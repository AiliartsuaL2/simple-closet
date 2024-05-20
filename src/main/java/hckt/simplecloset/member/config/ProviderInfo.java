package hckt.simplecloset.member.config;

import hckt.simplecloset.member.adapter.out.communicate.dto.OAuthCommunicateResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class ProviderInfo {
    @Setter
    private Class<? extends OAuthCommunicateResponseDto> responseType;

    private final String grantType = "authorization_code";
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl ;
    private final String infoUrl ;
    private final String logoutUrl ;
}
