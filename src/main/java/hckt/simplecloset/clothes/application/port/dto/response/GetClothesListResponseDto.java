package hckt.simplecloset.clothes.application.port.dto.response;

import hckt.simplecloset.clothes.domain.Category;
import hckt.simplecloset.clothes.domain.Clothes;

import java.util.List;
import java.util.stream.Collectors;

public record GetClothesListResponseDto (
    List<GetClothesResponseDto> outer,
    List<GetClothesResponseDto> top,
    List<GetClothesResponseDto> bottom,
    List<GetClothesResponseDto> bag,
    List<GetClothesResponseDto> shoes,
    List<GetClothesResponseDto> acc
){
    public static GetClothesListResponseDto of(List<Clothes> clothesList) {
        return new GetClothesListResponseDto(
                byCategory(clothesList, Category.OUTER),
                byCategory(clothesList, Category.TOP),
                byCategory(clothesList, Category.BOTTOM),
                byCategory(clothesList, Category.BAG),
                byCategory(clothesList, Category.SHOES),
                byCategory(clothesList, Category.ACC)
        );
    }

    private static List<GetClothesResponseDto> byCategory(List<Clothes> clothesList, Category category) {
        return clothesList.stream()
                .filter(clothes -> category.equals(clothes.getCategory()))
                .map(GetClothesResponseDto::of)
                .toList();
    }
}
