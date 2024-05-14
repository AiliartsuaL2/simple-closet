package hckt.simplecloset.global.application.port.out;

import hckt.simplecloset.global.domain.UserAccount;

public interface CommandUserAccountPort {
    void create(UserAccount userAccount);
}
