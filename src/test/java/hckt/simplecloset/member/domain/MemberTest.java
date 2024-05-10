package hckt.simplecloset.member.domain;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "testPassword";
    private static final Provider PROVIDER = Provider.GOOGLE;

    void assertThrow(ThrowableAssert.ThrowingCallable action, String errorMessage, Class<? extends Throwable> throwable) {
        Assertions.assertThatThrownBy(action)
                .isInstanceOf(throwable)
                .hasMessage(errorMessage);
    }

    @Nested
    @DisplayName("회원 생성 테스트")
    class Constructor {
        @Test
        @DisplayName("이메일 미존재시 예외가 발생한다")
        void test1() {
            // given
            String email = null;
            String password = PASSWORD;
            String errorMessage = ErrorMessage.NOT_EXIST_EMAIL.getMessage();

            // when & then
            assertThrow(() -> new Member(email, password, PROVIDER), errorMessage, IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비밀번호 미존재시 예외가 발생한다")
        void test2() {
            // given
            String email = EMAIL;
            String password = null;
            String errorMessage = ErrorMessage.NOT_EXIST_PASSWORD.getMessage();

            // when & then
            assertThrow(() -> new Member(email, password, PROVIDER), errorMessage, IllegalArgumentException.class);
        }

        @Test
        @DisplayName("소셜 타입 미존재시 예외가 발생한다")
        void test3() {
            // given
            String email = EMAIL;
            String password = PASSWORD;
            Provider provider = null;
            String errorMessage = ErrorMessage.NOT_EXIST_PROVIDER.getMessage();

            // when & then
            assertThrow(() -> new Member(email, password, provider), errorMessage, IllegalArgumentException.class);
        }


        @Test
        @DisplayName("정상 생성시 이메일이 할당되고, 비밀번호가 암호화 된다.")
        void test4() {
            // given
            String email = EMAIL;
            String password = PASSWORD;

            // when
            Member member = new Member(email, password, PROVIDER);

            // then
            assertThat(email).isEqualTo(member.getEmail());
            assertThat(password).isNotNull();
            assertThat(password).isNotEqualTo(member.getPassword());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class Login {
        @Test
        @DisplayName("비밀번호 불일치시 예외가 발생한다")
        void test1() {
            // given
            Member member = new Member(EMAIL, PASSWORD, PROVIDER);
            String password = PASSWORD + "abcde";

            // when & then
            assertThrow(() -> member.login(password),
                    ErrorMessage.NOT_MATCHED_PASSWORD.getMessage(),
                    IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비밀번호 일치시 예외가 발생하지 않는다.")
        void test2() {
            // given
            Member member = new Member(EMAIL, PASSWORD, PROVIDER);
            String password = PASSWORD;

            // when & then
            member.login(password);
        }
    }
}