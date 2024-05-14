package hckt.simplecloset.global.adapter.out.persistence;

import hckt.simplecloset.global.annotation.PersistenceAdapter;
import hckt.simplecloset.global.application.port.out.CommandUserAccountPort;
import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.UserAccount;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class UserAccountPersistenceAdapter implements LoadUserAccountPort, CommandUserAccountPort {

    private final UserAccountRepository userAccountRepository;

    @Override
    public Optional<UserAccount> findById(Long id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public Optional<UserAccount> findUserAccountByMemberId(Long memberId) {
        return userAccountRepository.findUserAccountByMemberId(memberId);
    }

    @Override
    public void create(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }
}
