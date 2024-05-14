package hckt.simplecloset.global.application.port.in;

import hckt.simplecloset.global.domain.UserAccount;

public interface CreateUserAccountUseCase {
    void create(UserAccount userAccount);
}
