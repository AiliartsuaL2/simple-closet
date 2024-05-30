package hckt.simplecloset.member.application.service;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.adapter.in.rest.dto.request.SignInAppleRequestDto;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.dto.out.GetOAuthInfoResponseDto;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.application.port.out.*;
import hckt.simplecloset.member.domain.Member;
import hckt.simplecloset.member.domain.OAuthInfo;
import hckt.simplecloset.member.exception.ErrorMessage;
import hckt.simplecloset.member.exception.OAuthSignInException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "testPassword";
    private static final String PICTURE = "picture";
    private static final String NICKNAME = "nickname";
    private static final String PROVIDER = "google";
    private static final String CODE = "code";
    CommandMemberPort commandMemberPort = mock(CommandMemberPort.class);
    LoadMemberPort loadMemberPort = mock(LoadMemberPort.class);
    ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    LoadOAuthInfoPort loadOAuthInfoPort = mock(LoadOAuthInfoPort.class);
    CommandOAuthInfoPort commandOAuthInfoPort = mock(CommandOAuthInfoPort.class);
    LoadTokenPort loadTokenPort = mock(LoadTokenPort.class);

    MemberService memberService = new MemberService(commandMemberPort, loadMemberPort, applicationEventPublisher, loadOAuthInfoPort, commandOAuthInfoPort, loadTokenPort);

    void assertThrow(ThrowableAssert.ThrowingCallable action, String errorMessage, Class<? extends Throwable> throwable) {
        Assertions.assertThatThrownBy(action)
                .isInstanceOf(throwable)
                .hasMessage(errorMessage);
    }

    @Nested
    @DisplayName("일반 회원가입 테스트")
    class CommonSignUp {
        @Test
        @DisplayName("이미 존재하는 이메일이 있는경우, 예외가 발생한다.")
        void test1() {
            // given
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto(EMAIL, PASSWORD, PICTURE, PROVIDER);
            when(loadMemberPort.findByEmail(EMAIL))
                    .thenReturn(Optional.of(mock(Member.class)));

            // when
            assertThrow(() -> memberService.signUp(signUpRequestDto),
                    ErrorMessage.ALREADY_EXIST_EMAIL.getMessage(),
                    IllegalArgumentException.class);
        }

        @Test
        @DisplayName("정상 회원가입시 commandMemberPort.save 호출")
        void test2() {
            // given
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto(EMAIL, PASSWORD, PICTURE,PROVIDER);

            // when
            memberService.signUp(signUpRequestDto);

            // then
            then(commandMemberPort)
                    .should(times(1))
                    .save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("일반 로그인 테스트")
    class CommonSignIn {
        @Test
        @DisplayName("회원 미존재시 예외가 발생한다.")
        void test1() {
            // given
            SignInRequestDto signInRequestDto = new SignInRequestDto(EMAIL, PASSWORD);

            // when & then
            assertThrow(() -> memberService.signIn(signInRequestDto),
                    ErrorMessage.NOT_EXIST_MEMBER.getMessage(),
                    IllegalArgumentException.class);
        }

        @Test
        @DisplayName("정상 로그인시 회원의 ID가 응답된다.")
        void test2() {
            // given
            SignInRequestDto signInRequestDto = new SignInRequestDto(EMAIL, PASSWORD);
            Provider provider = Provider.findByCode(PROVIDER);
            Member member = new Member(EMAIL, PASSWORD, PICTURE, provider);
            when(loadMemberPort.findByEmail(EMAIL))
                    .thenReturn(Optional.of(member));

            // when
            Long memberId = memberService.signIn(signInRequestDto);

            // then
            assertThat(memberId).isEqualTo(member.getId());
        }
    }

    @Nested
    @DisplayName("소셜 로그인 _ 구글,카카오 테스트")
    class OAuthSignIn {
        @Test
        @DisplayName("회원 미존재시 OAuthInfo가 저장되고 예외가 발생한다.")
        void test1() {
            // given
            OAuthSignInRequestDto requestDto = new OAuthSignInRequestDto(CODE, PROVIDER);
            OAuthInfo oAuthInfo = new OAuthInfo(Provider.GOOGLE, EMAIL, PICTURE, NICKNAME);
            when(loadOAuthInfoPort.loadOAuthInfo(requestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.GOOGLE))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> memberService.signIn(requestDto))
                    .isInstanceOf(OAuthSignInException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
            then(commandOAuthInfoPort).should(times(1)).save(oAuthInfo);
        }

        @Test
        @DisplayName("회원이 존재하는 경우 회원 ID를 반환한다.")
        void test2() {
            // given
            OAuthSignInRequestDto requestDto = new OAuthSignInRequestDto(CODE, PROVIDER);
            OAuthInfo oAuthInfo = new OAuthInfo(Provider.GOOGLE, EMAIL, PICTURE, NICKNAME);
            Member member = mock(Member.class);
            when(member.getId())
                    .thenReturn(ID);
            when(loadOAuthInfoPort.loadOAuthInfo(requestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.GOOGLE))
                    .thenReturn(Optional.of(member));

            // when
            Long memberId = memberService.signIn(requestDto);

            // then
            Assertions.assertThat(memberId).isEqualTo(ID);
        }
    }

    @Nested
    @DisplayName("소셜 로그인 _ 애플 테스트")
    class OAuthAppleSignIn {
        private final String ID_TOKEN = "idToken";
        @Test
        @DisplayName("회원 미존재시 OAuthInfo가 저장되고 예외가 발생한다.")
        void test1() {
            // given
            SignInAppleRequestDto requestDto = new SignInAppleRequestDto(CODE, ID_TOKEN);
            OAuthInfo oAuthInfo = new OAuthInfo(Provider.APPLE, EMAIL);
            when(loadOAuthInfoPort.loadOAuthInfo(requestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.APPLE))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> memberService.signIn(requestDto))
                    .isInstanceOf(OAuthSignInException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
            then(commandOAuthInfoPort).should(times(1)).save(oAuthInfo);
        }

        @Test
        @DisplayName("회원이 존재하는 경우 회원 ID를 반환한다.")
        void test2() {
            // given
            SignInAppleRequestDto requestDto = new SignInAppleRequestDto(CODE, ID_TOKEN);
            OAuthInfo oAuthInfo = new OAuthInfo(Provider.APPLE, EMAIL);
            Member member = mock(Member.class);
            when(member.getId())
                    .thenReturn(ID);
            when(loadOAuthInfoPort.loadOAuthInfo(requestDto))
                    .thenReturn(oAuthInfo);
            when(loadMemberPort.loadMemberByEmailAndProvider(EMAIL, Provider.APPLE))
                    .thenReturn(Optional.of(member));

            // when
            Long memberId = memberService.signIn(requestDto);

            // then
            Assertions.assertThat(memberId).isEqualTo(ID);
        }
    }

    @Nested
    @DisplayName("OAuth Info 조회 테스트")
    class GetToken {
        private final String uid = "uid";
        @Test
        @DisplayName("UID에 해당하는 OAuth Info 미존재시 예외가 발생한다.")
        void test1() {
            // given
            when(loadOAuthInfoPort.loadOAuthInfo(uid))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> memberService.getOAuthInfo(uid))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_OAUTH_INFO_BY_UID.getMessage());
        }

        @Test
        @DisplayName("존재하는 OAuthInfo 조회시 생성한 OAuthInfo가 Dto로 변환되어 반환된다.")
        void test2() {
            // given
            OAuthInfo oAuthInfo = new OAuthInfo(Provider.APPLE, EMAIL, PICTURE,  NICKNAME);
            when(loadOAuthInfoPort.loadOAuthInfo(uid))
                    .thenReturn(Optional.of(oAuthInfo));

            // when
            GetOAuthInfoResponseDto dto = memberService.getOAuthInfo(uid);

            // then
            assertThat(dto.email()).isEqualTo(EMAIL);
            assertThat(dto.image()).isEqualTo(PICTURE);
            assertThat(dto.nickname()).isEqualTo(NICKNAME);
        }
    }
}