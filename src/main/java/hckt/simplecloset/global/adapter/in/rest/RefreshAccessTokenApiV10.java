package hckt.simplecloset.global.adapter.in.rest;

import hckt.simplecloset.global.adapter.in.rest.dto.request.RefreshAccessTokenRequestDto;
import hckt.simplecloset.global.adapter.in.rest.dto.response.RefreshAccessTokenResponseDto;
import hckt.simplecloset.global.application.port.in.RenewAccessTokenUseCase;
import hckt.simplecloset.global.dto.ApiCommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class RefreshAccessTokenApiV10 {
    private final RenewAccessTokenUseCase renewAccessTokenUseCase;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiCommonResponse<RefreshAccessTokenResponseDto>> renewAccessToken(@RequestBody RefreshAccessTokenRequestDto requestDto) {
        String accessToken = renewAccessTokenUseCase.renewAccessToken(requestDto.refreshToken());
        return ResponseEntity.ok(new ApiCommonResponse<>(new RefreshAccessTokenResponseDto(accessToken), true));
    }
}
