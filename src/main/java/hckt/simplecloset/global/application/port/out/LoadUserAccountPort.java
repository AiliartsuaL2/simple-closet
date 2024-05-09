package hckt.simplecloset.global.application.port.out;

import hckt.simplecloset.global.domain.UserAccount;

import java.util.Optional;

public interface LoadUserAccountPort {
    Optional<UserAccount> findById(Long id);
}
