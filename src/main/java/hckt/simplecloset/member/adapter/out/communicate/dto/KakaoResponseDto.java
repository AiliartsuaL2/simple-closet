package hckt.simplecloset.member.adapter.out.communicate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoResponseDto extends OAuthCommunicateResponseDto {
    private String id;
    private String connectedAt;
    private KakaoPropertiesDto properties;
    private KakaoAccountDto kakaoAccount;

    @Getter
    @NoArgsConstructor
    private static class KakaoPropertiesDto {
        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Getter
    @NoArgsConstructor
    private static class KakaoAccountDto {
        private String email;
    }

    @Override
    public void initAllFields() {
        init(this.kakaoAccount.email, this.properties.profileImage, this.properties.nickname);
    }
}