package hckt.simplecloset.clothes.domain;

import hckt.simplecloset.clothes.application.port.dto.request.UpdateClothesRequestDto;
import hckt.simplecloset.clothes.exception.ErrorMessage;
import hckt.simplecloset.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesTest {
    private static final Long MEMBER_ID = 1L;
    private static final Category CATEGORY = Category.TOP;
    private static final String BRAND = "brand";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";

    @Nested
    @DisplayName("생성자 테스트")
    class Constructor {
        Long memberId = MEMBER_ID;
        String brand = BRAND;
        String category = CATEGORY.getValue();
        String name = NAME;
        String image = IMAGE;
        String price = PRICE;
        String description = DESCRIPTION;

        @Test
        @DisplayName("필수 인자 _ 회원 ID 미존재시 예외 발생")
        void test1() {
            // given
            memberId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new Clothes(memberId, category, brand, name, image, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("필수 인자 _ 브랜드 미존재시 예외 발생")
        void test2() {
            // given
            brand = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new Clothes(memberId, category, brand, name, image, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_BRAND.getMessage());
        }

        @Test
        @DisplayName("필수 인자 _ 이름 미존재시 예외 발생")
        void test3() {
            // given
            name = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new Clothes(memberId, category, brand, name, image, price, description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_NAME.getMessage());
        }

        @Test
        @DisplayName("정상 생성시 모든 필드 정상 매핑")
        void test4() {
            // given & when
            Clothes clothes = new Clothes(memberId, category, brand, name, image, price, description);

            // then
            assertThat(clothes.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(clothes.getCategory()).isEqualTo(CATEGORY);
            assertThat(clothes.getBrand()).isEqualTo(BRAND);
            assertThat(clothes.getName()).isEqualTo(NAME);
            assertThat(clothes.getImage()).isEqualTo(IMAGE);
            assertThat(clothes.getPrice()).isEqualTo(PRICE);
            assertThat(clothes.getDescription()).isEqualTo(DESCRIPTION);
        }
    }

    @Nested
    @DisplayName("수정 테스트")
    class Update {
        private final Category updatedCategory = Category.BOTTOM;
        private final String updatedBrand = "newBrand";
        private final String updatedName = "newName";
        private final String updatedImage = "newImage";
        private final String updatedPrice = "newPrice";
        private final String updatedDescription = "newDescription";

        Clothes clothes = new Clothes(MEMBER_ID, CATEGORY.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);
        Clothes forUpdate = new Clothes(MEMBER_ID, updatedCategory.getValue(), updatedBrand, updatedName, updatedImage, updatedPrice, updatedDescription);

        @Test
        @DisplayName("이미 삭제된 옷 수정시 예외가 발생한다.")
        void test1() {
            // given
            clothes.delete(MEMBER_ID);

            // when
            Assertions.assertThatThrownBy(() -> clothes.update(forUpdate, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_CLOTHES.getMessage());
        }

        @Test
        @DisplayName("수정하려는 회원이 해당 옷의 소유자가 아닌경우 예외가 발생한다.")
        void test2() {
            // given
            Long DifferentMemberId = 2L;

            // when
            Assertions.assertThatThrownBy(() -> clothes.update(forUpdate, DifferentMemberId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }

        @Test
        @DisplayName("정상 수정 요청시 수정된 데이터로 변경된다.")
        void test3() {

            // given & when
            clothes.update(forUpdate, MEMBER_ID);

            // then
            assertThat(clothes.getName()).isEqualTo(updatedName);
            assertThat(clothes.getBrand()).isEqualTo(updatedBrand);
            assertThat(clothes.getCategory()).isEqualTo(updatedCategory);
            assertThat(clothes.getImage()).isEqualTo(updatedImage);
            assertThat(clothes.getPrice()).isEqualTo(updatedPrice);
            assertThat(clothes.getDescription()).isEqualTo(updatedDescription);
        }
    }


    @Nested
    @DisplayName("삭제 테스트")
    class Delete {
        Clothes clothes = new Clothes(MEMBER_ID, CATEGORY.getValue(), BRAND, NAME, IMAGE, PRICE, DESCRIPTION);

        @Test
        @DisplayName("이미 삭제된 옷 삭제시 예외가 발생한다.")
        void test1() {
            // given
            clothes.delete(MEMBER_ID);

            // when
            Assertions.assertThatThrownBy(() -> clothes.delete(MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_CLOTHES.getMessage());
        }

        @Test
        @DisplayName("삭제하려는 회원이 해당 옷의 소유자가 아닌경우 예외가 발생한다.")
        void test2() {
            // given
            Long DifferentMemberId = 2L;

            // when
            Assertions.assertThatThrownBy(() -> clothes.delete(DifferentMemberId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_DELETE.getMessage());
        }

        @Test
        @DisplayName("정상 삭제 요청시 해당 옷의 상태가 삭제되어있고, 삭제 일자가 생성된다..")
        void test3() {
            // given & when
            clothes.delete(MEMBER_ID);

            // then
            assertThat(clothes.isDeleted()).isTrue();
            assertThat(clothes.getDeletedAt()).isNotNull();
        }
    }
}