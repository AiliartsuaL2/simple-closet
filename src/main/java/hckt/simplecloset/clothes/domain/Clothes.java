package hckt.simplecloset.clothes.domain;

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

    @Enumerated(EnumType.STRING)
    private Category category;

    private String brand;

    private String name;

    private String image;

    private String price;

    private String description;

    private boolean deleted;

    private LocalDateTime deletedAt;

    public Clothes(String category, String brand, String name, String image, String price, String description) {
        this.category = Category.findByValue(category);
        this.brand = brand;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.deleted = false;
    }

    public void delete() {
        if (this.deleted) {
            throw new IllegalArgumentException("존재하지 않는 옷이에요");
        }
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void update(Clothes clothes) {
        if (this.deleted) {
            throw new IllegalArgumentException("존재하지 않는 옷이에요");
        }
    }
}
