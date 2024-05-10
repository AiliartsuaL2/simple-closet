package hckt.simplecloset.member.adapter.out.persistence;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberPersistenceAdapterTest {
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "test";
    private static final Provider PROVIDER = Provider.GOOGLE;
    @Autowired
    MemberPersistenceAdapter memberPersistenceAdapter;
    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 저장 테스트")
    class Save {
        @Test
        @DisplayName("회원 저장시 ID가 생성된다.")
        void test1() {
            // given
            Member member = new Member(EMAIL, PASSWORD, PROVIDER);

            // when
            memberPersistenceAdapter.save(member);

            // then
            assertThat(member.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("이메일로 회원 조회 테스트")
    class FindByEmail {
        @Test
        @DisplayName("회원 미생성후 조회시 Optional.empty()가 true이다.")
        void test1() {
            // given & when
            Optional<Member> member = memberPersistenceAdapter.findByEmail(EMAIL);

            // then
            assertThat(member.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("회원 생성후 조회시 Optional.empty()가 false이다.")
        void test2() {
            // given
            Member member = new Member(EMAIL, PASSWORD, PROVIDER);
            memberRepository.save(member);

            // when
            Optional<Member> foundMember = memberPersistenceAdapter.findByEmail(EMAIL);

            // then
            assertThat(foundMember.isEmpty()).isFalse();
        }
    }
}