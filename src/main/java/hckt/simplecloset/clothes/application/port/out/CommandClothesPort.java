package hckt.simplecloset.clothes.application.port.out;

import hckt.simplecloset.clothes.domain.Clothes;

public interface CommandClothesPort {
    void save(Clothes clothes);
}
