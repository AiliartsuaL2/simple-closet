package hckt.simplecloset.global.adapter.out.persistence;

import hckt.simplecloset.global.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
