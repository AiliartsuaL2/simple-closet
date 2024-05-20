package hckt.simplecloset.member.adapter.out.persistence;

import hckt.simplecloset.global.annotation.PersistenceAdapter;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.application.port.out.CommandMemberPort;
import hckt.simplecloset.member.application.port.out.LoadMemberPort;
import hckt.simplecloset.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class MemberPersistenceAdapter implements CommandMemberPort, LoadMemberPort {
    private final MemberRepository memberRepository;

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Optional<Member> loadMemberByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findMemberByEmailAndProvider(email, provider);
    }
}
