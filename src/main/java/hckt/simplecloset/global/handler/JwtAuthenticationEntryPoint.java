package hckt.simplecloset.global.handler;

import hckt.simplecloset.global.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // unauthorized
        jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.UNAUTHORIZED.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
