package hckt.simplecloset.clothes.application.port.dto.request;

import hckt.simplecloset.clothes.domain.Clothes;
import hckt.simplecloset.clothes.exception.ErrorMessage;
import hckt.simplecloset.global.dto.ApplicationRequestDto;

public record CreateClothesRequestDto(
        String image,
        String category,
        String brand,
        String name,
        String price,
        String description
) implements ApplicationRequestDto {
    public CreateClothesRequestDto {
        requiredArgumentValidation(brand, ErrorMessage.NOT_EXIST_BRAND.getMessage());
        requiredArgumentValidation(name, ErrorMessage.NOT_EXIST_NAME.getMessage());
    }

    public Clothes toEntity() {
        return new Clothes(category, brand, name, image, price, description);
    }
}