package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.member.domain.Member;

public interface CommandMemberPort {
    void save(Member member);
}
