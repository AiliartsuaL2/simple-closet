package hckt.simplecloset.member.adapter.in.rest;

import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import hckt.simplecloset.member.application.dto.in.GetOAuthInfoRequestDto;
import hckt.simplecloset.member.application.port.in.GetOAuthInfoUseCase;
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

import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = GetOAuthInfoApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class GetOAuthInfoApiV10Test {
    private static final String PATH = "/api/v1.0/oauth/info/sign-up";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetOAuthInfoUseCase getOAuthInfoUseCase;

    @Test
    @DisplayName("UID 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 요청시 200이 응답된다.")
    void test2() throws Exception {
        // given
        String uid = "uid";
        String type = "sign-up";
        GetOAuthInfoRequestDto requestDto = new GetOAuthInfoRequestDto(uid, type);

        // when & then
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("uid", uid)
                        .with(csrf()))
                .andExpect(status().isOk());

        then(getOAuthInfoUseCase)
                .should()
                .getOAuthInfo(requestDto);
    }
}