package hckt.simplecloset.global.dto;

import org.springframework.util.ObjectUtils;

public interface ApplicationRequestDto {
    default <T> void requiredArgumentValidation(T data, String message) {
        if(ObjectUtils.isEmpty(data)) {
            throw new IllegalArgumentException(message);
        }
    }
}
