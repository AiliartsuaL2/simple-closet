package hckt.simplecloset.global.adapter.in.eventlistener;

import hckt.simplecloset.global.application.port.in.CreateUserAccountUseCase;
import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.UserAccount;
import hckt.simplecloset.global.dto.event.CreateUserAccountEvent;
import hckt.simplecloset.global.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@SpringBootTest
class CreateUserAccountEventListenerTest {
    CreateUserAccountUseCase createUserAccountUseCase = mock(CreateUserAccountUseCase.class);
    CreateUserAccountEventListener createUserAccountEventListener = new CreateUserAccountEventListener(createUserAccountUseCase);

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    LoadUserAccountPort loadUserAccountPort;

    @Nested
    @DisplayName("회원 정보 생성 테스트")
    class CreateUserAccountTest {
        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 생성된다.")
        void test1() {
            // given
            Long memberId = 0L;

            // when
            publisher.publishEvent(new CreateUserAccountEvent(memberId));
            Optional<UserAccount> userAccountByMemberId = loadUserAccountPort.findUserAccountByMemberId(memberId);

            // then
            Assertions.assertThat(userAccountByMemberId).isPresent();
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외 발생")
        void test2() {
            // given & when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 회원ID 필드가 null인 경우 예외 발생")
        void test3() {
            // given
            Long memberId = null;
            String roleType = "user";

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new CreateUserAccountEvent(memberId)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
    }
}