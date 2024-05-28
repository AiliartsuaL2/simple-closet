package hckt.simplecloset.member.domain;

import hckt.simplecloset.global.domain.BaseEntity;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.global.domain.converter.ProviderConverter;
import hckt.simplecloset.member.exception.ErrorMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="OAUTH_INFO")
public class OAuthInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36)
    private String uid;

    @Convert(converter = ProviderConverter.class)
    @Column(columnDefinition = "varchar(10)")
    private Provider provider;

    private String email;

    @Column(length = 2083)
    private String image;

    private String nickname;

    public OAuthInfo(Provider provider, String email, String image, String nickname) {
        if (ObjectUtils.isEmpty(email)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        }
        if (ObjectUtils.isEmpty(provider)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER.getMessage());
        }

        this.uid = UUID.randomUUID().toString().replace("-", "");
        this.provider = provider;
        this.email = email;
        this.image = image;
        this.nickname = nickname;
    }

    public OAuthInfo(Provider provider, String email) {
        if (ObjectUtils.isEmpty(email)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_EMAIL.getMessage());
        }
        if (ObjectUtils.isEmpty(provider)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PROVIDER.getMessage());
        }

        this.uid = UUID.randomUUID().toString().replace("-", "");
        this.provider = provider;
        this.email = email;
        //todo image gravatar 설정
        this.image = "";
        this.nickname = "익명";
    }
}
