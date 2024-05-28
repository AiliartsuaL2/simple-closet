package hckt.simplecloset.member.application.port.in;

import hckt.simplecloset.member.adapter.in.rest.dto.request.SignInAppleRequestDto;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;

public interface SignInUseCase {

    Long signIn(SignInRequestDto signInRequestDto);

    Long signIn(OAuthSignInRequestDto oAuthSignInRequestDto);

    Long signIn(SignInAppleRequestDto requestDto);
}
