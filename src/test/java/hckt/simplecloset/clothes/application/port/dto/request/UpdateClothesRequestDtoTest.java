package hckt.simplecloset.clothes.application.port.dto.request;

import hckt.simplecloset.clothes.domain.Category;
import hckt.simplecloset.clothes.domain.Clothes;
import hckt.simplecloset.clothes.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateClothesRequestDtoTest {

    private static final Long ID = 1L;
    private static final Category CATEGORY = Category.TOP;
    private static final String BRAND = "brand";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";

    @Nested
    @DisplayName("생성자 테스트")
    class Constructor {
        Long id = ID;
        String brand = BRAND;
        String category = CATEGORY.getValue();
        String name = NAME;
        String image = IMAGE;
        String price = PRICE;
        String description = DESCRIPTION;

        @Test
        @DisplayName("필수 인자 _ id 미존재시 예외 발생")
        void test1() {
            // given
            id = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new UpdateClothesRequestDto(id, image, category, brand, name, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_ID.getMessage());
        }

        @Test
        @DisplayName("필수 인자 _ 브랜드 미존재시 예외 발생")
        void test2() {
            // given
            brand = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new UpdateClothesRequestDto(id, image, category, brand, name, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_BRAND.getMessage());
        }

        @Test
        @DisplayName("필수 인자 _ 이름 미존재시 예외 발생")
        void test3() {
            // given
            name = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new UpdateClothesRequestDto(id, image, category, brand, name, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_NAME.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 필드가 정상 매핑된다.")
        void test4() {
            // given & when
            UpdateClothesRequestDto updateClothesRequestDto = new UpdateClothesRequestDto(id, image, category, brand, name, price, description);

            // then
            Assertions.assertThat(updateClothesRequestDto.image()).isEqualTo(image);
            Assertions.assertThat(updateClothesRequestDto.category()).isEqualTo(category);
            Assertions.assertThat(updateClothesRequestDto.brand()).isEqualTo(brand);
            Assertions.assertThat(updateClothesRequestDto.name()).isEqualTo(name);
            Assertions.assertThat(updateClothesRequestDto.price()).isEqualTo(price);
            Assertions.assertThat(updateClothesRequestDto.description()).isEqualTo(description);
        }
    }


    @Nested
    @DisplayName("엔티티 변환 테스트")
    class ToEntity {
        UpdateClothesRequestDto requestDto = new UpdateClothesRequestDto(ID, IMAGE, CATEGORY.getValue(), BRAND, NAME, PRICE, DESCRIPTION);

        @Test
        @DisplayName("회원 ID 미존재시 예외 발생")
        void test1() {
            // given
            Long memberId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> requestDto.toEntity(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 필드가 매핑된 엔티티가 반환된다.")
        void test2() {
            // given
            Long memberId = 1L;

            // when
            Clothes clothes = requestDto.toEntity(memberId);

            // then
            Assertions.assertThat(clothes.getMemberId()).isEqualTo(memberId);
            Assertions.assertThat(clothes.getCategory()).isEqualTo(CATEGORY);
            Assertions.assertThat(clothes.getBrand()).isEqualTo(BRAND);
            Assertions.assertThat(clothes.getName()).isEqualTo(NAME);
            Assertions.assertThat(clothes.getPrice()).isEqualTo(PRICE);
            Assertions.assertThat(clothes.getDescription()).isEqualTo(DESCRIPTION);
        }
    }
}