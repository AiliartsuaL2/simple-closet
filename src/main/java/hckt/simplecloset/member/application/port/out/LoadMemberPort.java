package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.domain.Member;

import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> findByEmail(String email);
    Optional<Member> loadMemberByEmailAndProvider(String email, Provider provider);
}
