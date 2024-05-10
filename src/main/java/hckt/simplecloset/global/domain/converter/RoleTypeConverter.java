package hckt.simplecloset.global.domain.converter;

import hckt.simplecloset.global.domain.RoleType;
import jakarta.persistence.AttributeConverter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType.getCode();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(RoleType.class).stream()
                .filter(e->e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
