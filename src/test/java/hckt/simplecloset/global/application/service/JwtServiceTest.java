package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.Token;
import hckt.simplecloset.global.domain.UserAccount;
import hckt.simplecloset.global.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {
    CreateTokenProvider createTokenProvider = mock(CreateTokenProvider.class);
    GetTokenInfoProvider getTokenInfoProvider = mock(GetTokenInfoProvider.class);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    LoadUserAccountPort loadUserAccountPort = mock(LoadUserAccountPort.class);
    JwtService jwtService = new JwtService(createTokenProvider, getTokenInfoProvider, userDetailsService, loadUserAccountPort);

    @Nested
    @DisplayName("토큰 생성 테스트")
    class Create {
        @Test
        @DisplayName("회원 Id 미존재시 예외가 발생한다.")
        void test1() {
            // given
            Long userAccountId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.create(userAccountId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("가입되지 않은 회원 (userAccount가 없는경우)인 경우 예외가 발생한다.")
        void test2() {
            // given
            Long userAccountId = 0L;
            when(loadUserAccountPort.findById(userAccountId))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.create(userAccountId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        @Test
        @DisplayName("정상 토큰 생성 요청시 토큰이 발급된다.")
        void test3() {
            // given
            Long userAccountId = 0L;
            UserAccount userAccount = mock(UserAccount.class);
            when(loadUserAccountPort.findById(userAccountId))
                    .thenReturn(Optional.of(userAccount));

            // when
            Token token = jwtService.create(userAccountId);

            // then
            Assertions.assertThat(token.getAccessToken())
                    .isNotBlank();
            Assertions.assertThat(token.getRefreshToken())
                    .isNotBlank();
        }
    }

    @Nested
    @DisplayName("payload 추출 테스트")
    class ExtractPayload {

    }

    @Nested
    @DisplayName("액세스 토큰 재생성 테스트")
    class RenewAccessToken {

    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    class IsValid {

    }

    @Nested
    @DisplayName("Authentication 조회 테스트")
    class GetAuthentication {

    }
}