package hckt.simplecloset.global.domain;

import hckt.simplecloset.global.domain.converter.CodeValue;
import hckt.simplecloset.global.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Provider implements CodeValue {
    GOOGLE("google", "구글"),
    APPLE("apple", "애플"),
    KAKAO("kakao", "카카오"),
    COMMON("common", "일반");

    private final String code;
    private final String value;

    public static Provider findByCode(String code) {
        for(Provider provider : Provider.values()) {
            if(provider.code.equals(code)) {
                return provider;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER_TYPE.getMessage());
    }
}
