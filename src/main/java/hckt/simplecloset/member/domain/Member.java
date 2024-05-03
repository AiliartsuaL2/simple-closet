package hckt.simplecloset.member.domain;

import hckt.simplecloset.global.domain.BaseEntity;
import hckt.simplecloset.member.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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

    @Transient
    List<OAuth> oAuths = new ArrayList<>();
    @Transient
    List<Role> roles = new ArrayList<>();

    // 회원 생성
    public Member(String email, String password) {
        notNullValidation(email, ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        notNullValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        this.email = email;
        this.password = B_CRYPT_PASSWORD_ENCODER.encode(password);
    }

    // 로그인
    public void login(String password) {
        if (!B_CRYPT_PASSWORD_ENCODER.matches(password, this.password)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }
    }
}