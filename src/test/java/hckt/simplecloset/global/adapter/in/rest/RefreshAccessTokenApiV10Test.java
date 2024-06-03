package hckt.simplecloset.global.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hckt.simplecloset.global.adapter.in.rest.dto.request.RefreshAccessTokenRequestDto;
import hckt.simplecloset.global.adapter.in.rest.dto.response.RefreshAccessTokenResponseDto;
import hckt.simplecloset.global.application.port.in.RenewAccessTokenUseCase;
import hckt.simplecloset.global.dto.ApiCommonResponse;
import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = RefreshAccessTokenApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class RefreshAccessTokenApiV10Test {
    private static final String PATH = "/api/v1.0/token/refresh";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    RenewAccessTokenUseCase renewAccessTokenUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("RefreshToken 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 요청시, Status 200, access Token이 발급된다.")
    void test2() throws Exception {
        // given
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";

        RefreshAccessTokenRequestDto requestDto = new RefreshAccessTokenRequestDto(refreshToken);
        RefreshAccessTokenResponseDto tokenResponseDto = new RefreshAccessTokenResponseDto(accessToken);
        ApiCommonResponse<RefreshAccessTokenResponseDto> responseDto = new ApiCommonResponse<>(tokenResponseDto, true);
        String responseJson = new Gson().toJson(responseDto);

        when(renewAccessTokenUseCase.renewAccessToken(refreshToken))
                .thenReturn(accessToken);

        // when
        String response = mockMvc.perform(post(PATH)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(response)
                .isEqualTo(responseJson);
    }
}