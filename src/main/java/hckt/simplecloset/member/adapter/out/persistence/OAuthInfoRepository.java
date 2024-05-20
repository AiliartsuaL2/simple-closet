package hckt.simplecloset.member.adapter.out.persistence;

import hckt.simplecloset.member.domain.OAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthInfoRepository extends JpaRepository<OAuthInfo, Long> {
    Optional<OAuthInfo> findOAuthInfoByUid(String uid);
}
