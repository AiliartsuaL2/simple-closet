package hckt.simplecloset.clothes.application.port.in;

import hckt.simplecloset.clothes.application.port.dto.request.UpdateClothesRequestDto;

public interface UpdateClothesUseCase {
    void update(UpdateClothesRequestDto requestDto);
}
