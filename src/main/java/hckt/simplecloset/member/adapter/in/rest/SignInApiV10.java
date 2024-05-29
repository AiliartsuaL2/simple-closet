package hckt.simplecloset.member.adapter.in.rest;

import hckt.simplecloset.global.dto.ApiCommonResponse;
import hckt.simplecloset.member.adapter.in.rest.dto.request.SignInAppleRequestDto;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.application.port.in.GetTokenUseCase;
import hckt.simplecloset.member.application.port.in.SignInUseCase;
import hckt.simplecloset.member.exception.OAuthSignInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/api/v1.0")
public class SignInApiV10 {
    private final String customScheme;
    private final String singInPath;
    private final String signUpPath;
    private final SignInUseCase signInUseCase;
    private final GetTokenUseCase getTokenUseCase;

    public SignInApiV10(SignInUseCase signInUseCase,
                        GetTokenUseCase getTokenUseCase,
                        @Value("${simple-closet.app.custom-scheme}")
                        String customScheme,
                        @Value("${simple-closet.app.path.sign-in}")
                        String singInPath,
                        @Value("${simple-closet.app.path.sign-up}")
                        String signUpPath) {
        this.signInUseCase = signInUseCase;
        this.getTokenUseCase = getTokenUseCase;
        this.customScheme = customScheme;
        this.singInPath = singInPath;
        this.signUpPath = signUpPath;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiCommonResponse<String>> signIn(@RequestBody SignInRequestDto requestDto) {
        Long memberId = signInUseCase.signIn(requestDto);
        // todo OAuth, JWT 연동 후 토큰 발급 로직 추가
        HttpHeaders headers = new HttpHeaders();
        GetTokenResponseDto token = getTokenUseCase.getToken(memberId);
        headers.setLocation(getSignInUri(token));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(value = "/oauth/apple")
    public ResponseEntity<?> signIn(SignInAppleRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Long memberId = signInUseCase.signIn(requestDto);
            GetTokenResponseDto token = getTokenUseCase.getToken(memberId);
            headers.setLocation(getSignInUri(token));
        } catch (OAuthSignInException ex) {
            String uidParameter = ex.getUidParameter();
            headers.setLocation(getSignUpUri(uidParameter));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/oauth/{provider}")
    public ResponseEntity<?> signIn(@PathVariable String provider, @RequestParam String code) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Long memberId = signInUseCase.signIn(new OAuthSignInRequestDto(code, provider));
            GetTokenResponseDto token = getTokenUseCase.getToken(memberId);
            headers.setLocation(getSignInUri(token));
        } catch (OAuthSignInException ex) {
            String uidParameter = ex.getUidParameter();
            headers.setLocation(getSignUpUri(uidParameter));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private URI getSignInUri(GetTokenResponseDto token) {
        return URI.create(customScheme + singInPath + token.convertToQueryParam());
    }

    private URI getSignUpUri(String parameter) {
        return URI.create(customScheme + signUpPath + parameter);
    }
}
