package hckt.simplecloset.member.adapter.out.communicate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class ApplePublicKeyResponseDto {

    private List<Key> keys;

    @Getter
    @NoArgsConstructor
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    @Getter
    @NoArgsConstructor
    public static class Header {
        private String kid;
        private String alg;
    }

    public Optional<Key> getMatchedKeyBy(Header header) {
        if (header == null) {
            throw new NoSuchElementException("헤더가 존재하지 않아요");
        }
        return this.keys.stream()
                .filter(key -> key.getKid().equals(header.kid) && key.getAlg().equals(header.alg))
                .findFirst();
    }
}
