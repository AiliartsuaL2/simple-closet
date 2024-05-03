package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.member.domain.Member;

import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> findByEmail(String email);
}
