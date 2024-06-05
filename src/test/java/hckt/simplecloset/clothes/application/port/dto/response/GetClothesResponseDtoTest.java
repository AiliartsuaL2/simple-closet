package hckt.simplecloset.clothes.application.port.dto.response;

import hckt.simplecloset.clothes.domain.Clothes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetClothesResponseDtoTest {
    private static final String BRAND = "brand";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final LocalDateTime NOW =  LocalDateTime.now();

    @Test
    @DisplayName("정상적인 팩토리 메서드 요청시 필드가 정상 매핑된다.")
    void test1() {
        // given
        Clothes clothes = mock(Clothes.class);
        when(clothes.getName())
                .thenReturn(NAME);
        when(clothes.getBrand())
                .thenReturn(BRAND);
        when(clothes.getImage())
                .thenReturn(IMAGE);
        when(clothes.getCreatedDate())
                .thenReturn(NOW);

        // when
        GetClothesResponseDto responseDto = GetClothesResponseDto.of(clothes);

        // then
        assertThat(responseDto.name()).isEqualTo(NAME);
        assertThat(responseDto.brand()).isEqualTo(BRAND);
        assertThat(responseDto.image()).isEqualTo(IMAGE);
        assertThat(responseDto.createdAt()).isEqualTo(NOW);
    }
}