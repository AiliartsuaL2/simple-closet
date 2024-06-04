package hckt.simplecloset.clothes.application.port.out;

import hckt.simplecloset.clothes.domain.Clothes;

import java.util.List;
import java.util.Optional;

public interface LoadClothesPort {
    Optional<Clothes> findById(Long id);
    List<Clothes> findByType(String type);
}
