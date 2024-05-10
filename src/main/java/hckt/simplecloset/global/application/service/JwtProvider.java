package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.exception.ErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Component
class JwtProvider implements CreateTokenProvider, GetTokenInfoProvider {
    private final String secretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expired-time.access-token}") long accessTokenValidTime,
            @Value("${jwt.expired-time.refresh-token}") long refreshTokenValidTime) {
        this.secretKey = secretKey;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    @Override
    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidTime);
    }

    @Override
    public String createRefreshToken(String payload) {
        return createToken(payload, refreshTokenValidTime);
    }

    private String createToken(final String payload, final Long validityInMilliseconds) {
        if (ObjectUtils.isEmpty(payload)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String getPayload(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
