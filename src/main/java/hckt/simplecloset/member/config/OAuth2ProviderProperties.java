package hckt.simplecloset.member.config;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.adapter.out.communicate.dto.GoogleResponseDto;
import hckt.simplecloset.member.adapter.out.communicate.dto.KakaoResponseDto;
import hckt.simplecloset.member.exception.ErrorMessage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "oauth")
public class OAuth2ProviderProperties {
    private final ProviderInfo kakao;
    private final ProviderInfo google;
    private final ProviderInfo apple;

    @ConstructorBinding
    public OAuth2ProviderProperties(ProviderInfo kakao, ProviderInfo google, ProviderInfo apple) {
        this.kakao = kakao;
        this.google = google;
        this.apple = apple;
        kakao.setResponseType(KakaoResponseDto.class);
        google.setResponseType(GoogleResponseDto.class);
    }

    public ProviderInfo get(Provider provider) {
        if(Provider.KAKAO.equals(provider)) {
            return kakao;
        }
        if(Provider.APPLE.equals(provider)) {
            return apple;
        }
        if(Provider.GOOGLE.equals(provider)) {
            return google;
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER.getMessage());
    }
}

