package hckt.simplecloset.member.domain;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OAuthInfoTest {
    private static final String EMAIL = "email";
    private static final Provider PROVIDER = Provider.GOOGLE;
    private static final String IMAGE = "picture";
    private static final String NICKNAME = "nickname";
    private static final String ANONYMOUS_NICKNAME = "익명";
    private static final String RANDOM_PICTURE_URL = "https://www.gravatar.com/avatar/";

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {
        @Test
        @DisplayName("이메일 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String email = "";
            Provider provider = PROVIDER;
            String image = IMAGE;
            String nickname = NICKNAME;

            // when & then
            Assertions.assertThatThrownBy(() -> new OAuthInfo(provider, email, image, nickname))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        }

        @Test
        @DisplayName("provider 미존재시 예외가 발생한다.")
        void test2() {
            // given
            String email = EMAIL;
            Provider provider = null;
            String image = IMAGE;
            String nickname = NICKNAME;

            // when & then
            Assertions.assertThatThrownBy(() -> new OAuthInfo(provider, email, image, nickname))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PROVIDER.getMessage());
        }

        @Test
        @DisplayName("이미지 미존재시 랜덤 이미지로 설정된다.")
        void test3() {
            // given
            String email = EMAIL;
            Provider provider = PROVIDER;
            String image = "";
            String nickname = NICKNAME;

            // when
            OAuthInfo oAuthInfo = new OAuthInfo(provider, email, image, nickname);

            // then
            Assertions.assertThat(oAuthInfo.getImage()).contains(RANDOM_PICTURE_URL);
        }

        @Test
        @DisplayName("닉네임 미존재시 닉네임이 익명으로 설정된다.")
        void test4() {
            // given
            String email = EMAIL;
            Provider provider = PROVIDER;
            String image = IMAGE;
            String nickname = "";

            // when
            OAuthInfo oAuthInfo = new OAuthInfo(provider, email, image, nickname);

            // then
            Assertions.assertThat(oAuthInfo.getNickname()).isEqualTo(ANONYMOUS_NICKNAME);
        }


        @Test
        @DisplayName("정상 생성시 모든 필드가 매핑된다.")
        void test5() {
            // given
            String email = EMAIL;
            Provider provider = PROVIDER;
            String image = IMAGE;
            String nickname = NICKNAME;

            // when
            OAuthInfo oAuthInfo = new OAuthInfo(provider, email, image, nickname);

            // then
            Assertions.assertThat(oAuthInfo.getEmail()).isEqualTo(email);
            Assertions.assertThat(oAuthInfo.getProvider()).isEqualTo(provider);
            Assertions.assertThat(oAuthInfo.getImage()).isEqualTo(image);
            Assertions.assertThat(oAuthInfo.getNickname()).isEqualTo(nickname);
        }
    }

}