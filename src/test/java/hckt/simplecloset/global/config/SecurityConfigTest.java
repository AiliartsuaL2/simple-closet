package hckt.simplecloset.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.simplecloset.global.adapter.in.AuthenticationTestController;
import hckt.simplecloset.global.application.service.JwtService;
import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@WebMvcTest(
        value = AuthenticationTestController.class,
        includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class})
)
@Import({
        JwtExceptionFilter.class,
        JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class
})
class SecurityConfigTest {

    private static final String NOT_USE_AUTHENTICATION_URL = "/api/v1/auth/not-use/test";
    private static final String USE_AUTHENTICATION_URL = "/api/v1/auth/use/test";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    JwtService jwtService;

    @BeforeAll
    static void init(JwtService jwtService) {
        when(jwtService.isValid(any()))
                .thenReturn(true);
    }

    @Nested
    @DisplayName("인증 필요 없는 요청 테스트")
    class NotUseAuthentication {
        @Test
        @DisplayName("토큰 없이 요청시 success가 응답된다.")
        void test1() throws Exception {
            // given
            String url = NOT_USE_AUTHENTICATION_URL;

            // when
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("인증 필요 요청 테스트")
    class UseAuthentication {
        @Test
        @DisplayName("토큰 없이 요청시 예외가 응답된다.")
        void test1() throws Exception {
            // given
            String url = USE_AUTHENTICATION_URL;

            // when
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }
    }
}