package hckt.simplecloset.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.simplecloset.global.dto.ApiErrorResponse;
import hckt.simplecloset.global.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtErrorResponseHandler {

    private final ObjectMapper objectMapper;

    public void generateJwtErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(message);
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
