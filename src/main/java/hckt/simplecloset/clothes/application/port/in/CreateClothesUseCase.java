package hckt.simplecloset.clothes.application.port.in;

import hckt.simplecloset.clothes.application.port.dto.request.CreateClothesRequestDto;
import hckt.simplecloset.clothes.domain.Clothes;

public interface CreateClothesUseCase {
    void create(CreateClothesRequestDto requestDto, Long memberId);
}
