package hckt.simplecloset.clothes.application.port.dto.response;

import hckt.simplecloset.clothes.domain.Category;
import hckt.simplecloset.clothes.domain.Clothes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GetClothesListResponseDtoTest {

    @Nested
    @DisplayName("정적 팩토리 메서드 테스트")
    class staticFactoryMethod {
        private static final Long MEMBER_ID = 1L;
        private static final Category CATEGORY_TOP = Category.TOP;
        private static final String BRAND = "brand";
        private static final String NAME = "name";
        private static final String IMAGE = "image";
        private static final String PRICE = "price";
        private static final String DESCRIPTION = "description";

        @Test
        @DisplayName("카테고리에 해당하는 옷이 없는경우 빈배열 반환")
        void test1() {
            // given
            List<Clothes> clothesList = new ArrayList<>();

            // when
            GetClothesListResponseDto responseDto = GetClothesListResponseDto.of(clothesList);

            // then
            Assertions.assertThat(responseDto.top()).size().isEqualTo(0);
            Assertions.assertThat(responseDto.bag()).size().isEqualTo(0);
            Assertions.assertThat(responseDto.shoes()).size().isEqualTo(0);
            Assertions.assertThat(responseDto.bottom()).size().isEqualTo(0);
            Assertions.assertThat(responseDto.outer()).size().isEqualTo(0);
            Assertions.assertThat(responseDto.acc()).size().isEqualTo(0);
        }

        @Test
        @DisplayName("카테고리에 옷이 있는경우, 해당 필드에 매핑된다.")
        void test2() {
            // given
            Clothes clothes = new Clothes(MEMBER_ID, CATEGORY_TOP.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);
            Clothes clothes2 = new Clothes(MEMBER_ID, CATEGORY_TOP.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);
            Clothes clothes3 = new Clothes(MEMBER_ID, CATEGORY_TOP.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);
            Clothes clothes4 = new Clothes(MEMBER_ID, CATEGORY_TOP.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);
            List<Clothes> clothesList = List.of(clothes, clothes2, clothes3, clothes4);

            // when
            GetClothesListResponseDto responseDto = GetClothesListResponseDto.of(clothesList);

            // then
            Assertions.assertThat(responseDto.top()).size().isEqualTo(4);
        }
    }
}