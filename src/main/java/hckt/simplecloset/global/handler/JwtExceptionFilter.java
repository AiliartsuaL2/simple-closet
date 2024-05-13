package hckt.simplecloset.global.handler;

import hckt.simplecloset.global.exception.ErrorMessage;
import hckt.simplecloset.global.exception.NotRegisteredUserAccountException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (SignatureException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.SIGNATURE_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.MALFORMED_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.EXPIRED_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotRegisteredUserAccountException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.INCORRECT_TOKEN_TYPE_EXCEPTION.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}