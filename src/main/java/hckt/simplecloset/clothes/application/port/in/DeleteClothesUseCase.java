package hckt.simplecloset.clothes.application.port.in;

public interface DeleteClothesUseCase {
    void delete(Long clothesId, Long memberId);
}
