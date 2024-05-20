package hckt.simplecloset.member.adapter.out.communicate.dto;

import lombok.Getter;

@Getter
public class OAuthToken {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expiresIn;
    private String scope;
    private int refreshTokenExpiresIn;
}
