package hckt.simplecloset.clothes.application.port.dto.request;

import hckt.simplecloset.clothes.domain.Clothes;
import hckt.simplecloset.clothes.exception.ErrorMessage;
import hckt.simplecloset.global.dto.ApplicationRequestDto;

public record UpdateClothesRequestDto(
        Long id,
        String image,
        String category,
        String brand,
        String name,
        String price,
        String description
) implements ApplicationRequestDto {
    public UpdateClothesRequestDto {
        requiredArgumentValidation(id, ErrorMessage.NOT_EXIST_ID.getMessage());
        requiredArgumentValidation(brand, ErrorMessage.NOT_EXIST_BRAND.getMessage());
        requiredArgumentValidation(name, ErrorMessage.NOT_EXIST_NAME.getMessage());
    }

    public Clothes toEntity(Long memberId) {
        requiredArgumentValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        return new Clothes(memberId, category, brand, name, image, price, description);
    }
}
