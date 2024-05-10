package hckt.simplecloset.member.domain;

import hckt.simplecloset.global.domain.BaseEntity;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.global.domain.converter.ProviderConverter;
import hckt.simplecloset.member.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
public class Member extends BaseEntity {
    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(length = 60)
    private String password;

    @Convert(converter = ProviderConverter.class)
    private Provider provider;

    // 회원 생성
    public Member(String email, String password, Provider provider) {
        notNullValidation(email, ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        notNullValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        notNullValidation(provider, ErrorMessage.NOT_EXIST_PROVIDER.getMessage());

        this.email = email;
        this.password = B_CRYPT_PASSWORD_ENCODER.encode(password);
        this.provider = provider;
    }

    // 로그인
    public Long login(String password) {
        if (!B_CRYPT_PASSWORD_ENCODER.matches(password, this.password)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }
        return this.id;
    }
}