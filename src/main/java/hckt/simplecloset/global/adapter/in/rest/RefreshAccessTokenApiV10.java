package hckt.simplecloset.global.adapter.in.rest;

import hckt.simplecloset.global.adapter.in.rest.dto.response.RefreshAccessTokenResponseDto;
import hckt.simplecloset.global.application.port.in.RenewAccessTokenUseCase;
import hckt.simplecloset.global.dto.ApiCommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class RefreshAccessTokenApiV10 {
    private final RenewAccessTokenUseCase renewAccessTokenUseCase;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiCommonResponse<RefreshAccessTokenResponseDto>> renewAccessToken(
            @RequestHeader(value = "Authorization")
            String refreshToken
    ) {
        String accessToken = renewAccessTokenUseCase.renewAccessToken(refreshToken);
        return ResponseEntity.ok(new ApiCommonResponse<>(new RefreshAccessTokenResponseDto(accessToken), true));
    }
}
