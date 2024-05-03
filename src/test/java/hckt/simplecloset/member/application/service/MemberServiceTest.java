package hckt.simplecloset.member.application.service;

import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.port.out.CommandMemberPort;
import hckt.simplecloset.member.application.port.out.LoadMemberPort;
import hckt.simplecloset.member.domain.Member;
import hckt.simplecloset.member.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "testPassword";
    CommandMemberPort commandMemberPort = mock(CommandMemberPort.class);
    LoadMemberPort loadMemberPort = mock(LoadMemberPort.class);

    MemberService memberService = new MemberService(commandMemberPort, loadMemberPort);

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
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto(EMAIL, PASSWORD);
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
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto(EMAIL, PASSWORD);

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
            Member member = new Member(EMAIL, PASSWORD);
            when(loadMemberPort.findByEmail(EMAIL))
                    .thenReturn(Optional.of(member));

            // when
            Long memberId = memberService.signIn(signInRequestDto);

            // then
            assertThat(memberId).isEqualTo(member.getId());
        }
    }
}