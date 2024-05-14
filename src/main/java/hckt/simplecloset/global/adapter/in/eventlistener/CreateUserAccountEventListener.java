package hckt.simplecloset.global.adapter.in.eventlistener;

import hckt.simplecloset.global.annotation.Event;
import hckt.simplecloset.global.application.port.in.CreateUserAccountUseCase;
import hckt.simplecloset.global.domain.UserAccount;
import hckt.simplecloset.global.dto.event.CreateUserAccountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@Event
@RequiredArgsConstructor
public class CreateUserAccountEventListener {
    private final CreateUserAccountUseCase createUserAccountUseCase;

    @EventListener(CreateUserAccountEvent.class)
    public void createUserAccount(CreateUserAccountEvent event) {
        createUserAccountUseCase.create(new UserAccount(event.getMemberId(), event.getRole()));
    }
}
