package hckt.simplecloset.clothes.application.port.in;

import hckt.simplecloset.clothes.application.port.dto.response.GetClothesListResponseDto;

public interface GetClothesUseCase {
    GetClothesListResponseDto getClothes(String type);
}
