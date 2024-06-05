package hckt.simplecloset.clothes.domain;

import hckt.simplecloset.clothes.exception.ErrorMessage;
import hckt.simplecloset.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "clothes")
public class Clothes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String brand;

    private String name;

    private String image;

    private String price;

    private String description;

    private boolean deleted;

    private LocalDateTime deletedAt;

    @Transient
    private MemberInfo memberInfo;

    public Clothes(Long memberId, String category, String brand, String name, String image, String price, String description) {
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        notNullValidation(brand, ErrorMessage.NOT_EXIST_BRAND.getMessage());
        notNullValidation(name, ErrorMessage.NOT_EXIST_NAME.getMessage());

        this.memberId = memberId;
        this.category = Category.findByValue(category);
        this.brand = brand;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.deleted = false;
    }

    public void delete(Long memberId) {
        if (this.deleted) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_CLOTHES.getMessage());
        }
        if (!this.memberId.equals(memberId)) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void update(Clothes clothes, Long memberId) {
        if (this.deleted) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_CLOTHES.getMessage());
        }
        if (!this.memberId.equals(memberId)) {
            throw new IllegalStateException(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }
        this.category = clothes.category;
        this.brand = clothes.brand;
        this.name = clothes.name;
        this.image = clothes.image;
        this.price = clothes.price;
        this.description = clothes.description;
    }
}
