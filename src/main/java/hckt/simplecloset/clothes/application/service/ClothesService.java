package hckt.simplecloset.clothes.application.service;

import hckt.simplecloset.clothes.application.port.dto.request.CreateClothesRequestDto;
import hckt.simplecloset.clothes.application.port.dto.request.UpdateClothesRequestDto;
import hckt.simplecloset.clothes.application.port.dto.response.GetClothesListResponseDto;
import hckt.simplecloset.clothes.application.port.dto.response.GetClothesResponseDto;
import hckt.simplecloset.clothes.application.port.in.CreateClothesUseCase;
import hckt.simplecloset.clothes.application.port.in.DeleteClothesUseCase;
import hckt.simplecloset.clothes.application.port.in.GetClothesUseCase;
import hckt.simplecloset.clothes.application.port.in.UpdateClothesUseCase;
import hckt.simplecloset.clothes.application.port.out.CommandClothesPort;
import hckt.simplecloset.clothes.application.port.out.LoadClothesPort;
import hckt.simplecloset.clothes.domain.Clothes;
import hckt.simplecloset.clothes.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesService implements CreateClothesUseCase, GetClothesUseCase, UpdateClothesUseCase, DeleteClothesUseCase {
    private final CommandClothesPort commandClothesPort;
    private final LoadClothesPort loadClothesPort;

    @Override
    @Transactional
    public void create(CreateClothesRequestDto requestDto) {
        Clothes clothes = requestDto.toEntity();
        commandClothesPort.save(clothes);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Clothes clothes = loadClothesPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_CLOTHES.getMessage()));
        clothes.delete();
    }

    @Override
    public GetClothesListResponseDto getClothes(String type) {
        List<Clothes> clothesList = loadClothesPort.findByType(type);
        return GetClothesListResponseDto.of(clothesList);
    }

    @Override
    @Transactional
    public void update(UpdateClothesRequestDto requestDto) {
        Clothes clothes = loadClothesPort.findById(requestDto.id())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_CLOTHES.getMessage()));
        clothes.update(requestDto.toEntity());
    }
}
