package hckt.simplecloset.member.adapter.out.persistence;

import hckt.simplecloset.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);
}
