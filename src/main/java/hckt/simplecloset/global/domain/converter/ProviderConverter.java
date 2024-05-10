package hckt.simplecloset.global.domain.converter;

import hckt.simplecloset.global.domain.Provider;
import jakarta.persistence.AttributeConverter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public class ProviderConverter implements AttributeConverter<Provider, String> {
    @Override
    public String convertToDatabaseColumn(Provider provider) {
        return provider.getCode();
    }

    @Override
    public Provider convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Provider.class).stream()
                .filter(e->e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
