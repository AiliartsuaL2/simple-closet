package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.application.port.in.*;
import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.Token;
import hckt.simplecloset.global.exception.ErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Component
public class JwtService implements CreateTokenUseCase, ExtractPayloadUseCase, RenewAccessTokenUseCase, ValidateTokenUseCase, GetAuthenticationUseCase {
    private final String secretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;
    private final UserDetailsService userDetailsService;
    private final LoadUserAccountPort loadUserAccountPort;

    public JwtService(@Value("${jwt.secret-key}") String secretKey,
                      @Value("${jwt.expired-time.access-token}") long accessTokenValidTime,
                      @Value("${jwt.expired-time.refresh-token}") long refreshTokenValidTime,
                      UserDetailsService userDetailsService,
                      LoadUserAccountPort loadUserAccountPort) {
        this.secretKey = secretKey;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
        this.userDetailsService = userDetailsService;
        this.loadUserAccountPort = loadUserAccountPort;
    }

    @Override
    public Token create(Long userAccountId) {
        if (ObjectUtils.isEmpty(userAccountId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        if (loadUserAccountPort.findById(userAccountId).isEmpty()) {
            throw new IllegalStateException(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        String payload = String.valueOf(userAccountId);
        return new Token(createAccessToken(payload), createRefreshToken(payload));
    }

    @Override
    public Long extractPayload(String accessToken) {
        return Long.valueOf(getPayload(accessToken));
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        String payload = getPayload(refreshToken);
        return createAccessToken(payload);
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

    @Override
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getPayload(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getPayload(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String createAccessToken(final String payload) {
        return createToken(payload, accessTokenValidTime);
    }

    private String createRefreshToken(final String payload) {
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
}
