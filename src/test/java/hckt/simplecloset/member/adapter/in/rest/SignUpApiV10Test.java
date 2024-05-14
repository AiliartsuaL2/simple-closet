package hckt.simplecloset.member.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.port.in.SignUpUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = SignUpApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class SignUpApiV10Test {
    private static final String SIGN_UP_PATH = "/api/v1.0/sign-up";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String PROVIDER = "google";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignUpUseCase signUpUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 DTO 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(post(SIGN_UP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 요청시 201이 응답된다.")
    void test2() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(EMAIL, PASSWORD, PROVIDER);

        // when & then
        mockMvc.perform(post(SIGN_UP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .with(csrf()))
                .andExpect(status().isCreated());

        then(signUpUseCase)
                .should()
                .signUp(signUpRequestDto);
    }
}