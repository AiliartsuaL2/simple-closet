package hckt.simplecloset.global.adapter.out.persistence;

import hckt.simplecloset.global.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findUserAccountByMemberId(Long memberId);
}
