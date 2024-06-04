package hckt.simplecloset.clothes.domain;

import hckt.simplecloset.clothes.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    OUTER("Outer"),
    TOP("Top"),
    BOTTOM("Bottom"),
    BAG("Bag"),
    SHOES("Shoes"),
    ACC("Acc");

    private final String value;

    public static Category findByValue(String category) {
        for (Category c : Category.values()) {
            if (c.value.equals(category)) {
                return c;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_CATEGORY.getMessage());
    }
}
