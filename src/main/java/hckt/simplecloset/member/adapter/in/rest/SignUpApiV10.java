package hckt.simplecloset.member.adapter.in.rest;

import hckt.simplecloset.global.dto.ApiCommonResponse;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.port.in.SignUpUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1.0")
public class SignUpApiV10 {

     private final SignUpUseCase signUpUseCase;

     @PostMapping("/sign-up")
     public ResponseEntity<ApiCommonResponse<String>> signUp(@RequestBody SignUpRequestDto requestDto) {
          signUpUseCase.signUp(requestDto);
          String message = "회원가입에 성공하였어요";
          ApiCommonResponse<String> response = new ApiCommonResponse<>(message, true);
          return new ResponseEntity<>(response, HttpStatus.CREATED);
     }
}
