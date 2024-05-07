package hckt.simplecloset.member.adapter.in.rest;

import hckt.simplecloset.global.dto.ApiCommonResponse;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.port.in.SignInUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1.0")
public class SignInApiV10 {
    private final SignInUseCase signInUseCase;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiCommonResponse<String>> signIn(@RequestBody SignInRequestDto requestDto) {
        Long memberId = signInUseCase.signIn(requestDto);
        // todo OAuth, JWT 연동 후 토큰 발급 로직 추가
        String message = "로그인에 성공하였어요";
        ApiCommonResponse<String> response = new ApiCommonResponse<>(message, true);
        return ResponseEntity.ok(response);
    }
}
