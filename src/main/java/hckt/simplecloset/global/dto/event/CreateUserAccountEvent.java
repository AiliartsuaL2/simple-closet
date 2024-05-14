package hckt.simplecloset.global.dto.event;

import hckt.simplecloset.global.domain.RoleType;
import lombok.Getter;

@Getter
public class CreateUserAccountEvent {
    private final Long memberId;
    private final String role;

    public CreateUserAccountEvent(Long memberId) {
        this.memberId = memberId;
        this.role = RoleType.ROLE_USER.getAuthority();
    }
}
