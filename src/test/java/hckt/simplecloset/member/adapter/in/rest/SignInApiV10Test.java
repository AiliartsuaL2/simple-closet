package hckt.simplecloset.member.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.application.port.in.GetTokenUseCase;
import hckt.simplecloset.member.application.port.in.SignInUseCase;
import hckt.simplecloset.member.application.port.in.SignUpUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = SignInApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class SignInApiV10Test {
    private static final String SIGN_IN_PATH = "/api/v1.0/sign-in";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @Value("${simple-closet.app.custom-scheme}")
    String customScheme;
    @Value("${simple-closet.app.path.sign-in}")
    String signInPath;
    @Value("${simple-closet.app.path.sign-up}")
    String signUpPath;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignInUseCase signInUseCase;
    @MockBean
    private GetTokenUseCase getTokenUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("로그인 DTO 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(post(SIGN_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 로그인 요청시 앱 커스텀 스킴으로 리다이렉트 된다.")
    void test2() throws Exception {
        // given
        SignInRequestDto signInRequestDto = new SignInRequestDto(EMAIL, PASSWORD);
        GetTokenResponseDto token = new GetTokenResponseDto(ACCESS_TOKEN, REFRESH_TOKEN);
        String expectedRedirectUrl = customScheme + signInPath + token.convertToQueryParam();
        when(getTokenUseCase.getToken(any()))
                .thenReturn(token);

        // when & then
        mockMvc.perform(post(SIGN_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto))
                        .with(csrf()))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl(expectedRedirectUrl));


        then(signInUseCase)
                .should()
                .signIn(signInRequestDto);
    }
}