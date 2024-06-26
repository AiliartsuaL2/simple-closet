package hckt.simplecloset.member.adapter.in.rest;

import hckt.simplecloset.global.dto.ApiCommonResponse;
import hckt.simplecloset.member.application.dto.in.GetOAuthInfoRequestDto;
import hckt.simplecloset.member.application.dto.out.GetOAuthInfoResponseDto;
import hckt.simplecloset.member.application.port.in.GetOAuthInfoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class GetOAuthInfoApiV10 {
    private final GetOAuthInfoUseCase getOAuthInfoUseCase;

    @GetMapping("/oauth/info/{type}")
    public ResponseEntity<ApiCommonResponse<GetOAuthInfoResponseDto>> loadInfo(@PathVariable String type, @RequestParam String uid) {
        GetOAuthInfoResponseDto oAuthInfo = getOAuthInfoUseCase.getOAuthInfo(new GetOAuthInfoRequestDto(uid, type));
        ApiCommonResponse<GetOAuthInfoResponseDto> apiCommonResponse = new ApiCommonResponse<>(oAuthInfo, true);
        return ResponseEntity.ok(apiCommonResponse);
    }
}
