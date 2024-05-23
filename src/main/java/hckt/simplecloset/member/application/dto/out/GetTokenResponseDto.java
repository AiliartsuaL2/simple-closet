package hckt.simplecloset.member.application.dto.out;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public record GetTokenResponseDto(String accessToken, String refreshToken){
    public String convertToQueryParam() {
        return "?token=" + URLEncoder.encode(toString(), StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
