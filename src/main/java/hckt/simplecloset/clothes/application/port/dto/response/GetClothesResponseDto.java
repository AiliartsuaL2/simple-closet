package hckt.simplecloset.clothes.application.port.dto.response;

import hckt.simplecloset.clothes.domain.Clothes;

import java.time.LocalDateTime;

public record GetClothesResponseDto(
        String name,
        String brand,
        String image,
        LocalDateTime createdAt
) {
    public static GetClothesResponseDto of(Clothes clothes) {
        return new GetClothesResponseDto(clothes.getName(), clothes.getBrand(), clothes.getImage(), clothes.getCreatedDate());
    }
}
