package hckt.simplecloset.member.application.service;

import hckt.simplecloset.global.annotation.UseCase;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.global.dto.event.CreateUserAccountEvent;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.application.port.in.GetTokenUseCase;
import hckt.simplecloset.member.application.port.in.SignInUseCase;
import hckt.simplecloset.member.application.port.in.SignUpUseCase;
import hckt.simplecloset.member.application.port.out.*;
import hckt.simplecloset.member.domain.Member;
import hckt.simplecloset.member.domain.OAuthInfo;
import hckt.simplecloset.member.exception.ErrorMessage;
import hckt.simplecloset.member.exception.OAuthSignInException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements SignInUseCase, SignUpUseCase, GetTokenUseCase {

    private final CommandMemberPort commandMemberPort;
    private final LoadMemberPort loadMemberPort;
    private final ApplicationEventPublisher eventPublisher;
    private final LoadOAuthInfoPort loadOAuthInfoPort;
    private final CommandOAuthInfoPort commandOAuthInfoPort;
    private final LoadTokenPort loadTokenPort;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (loadMemberPort.findByEmail(signUpRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_EXIST_EMAIL.getMessage());
        }

        Member member = new Member(
                signUpRequestDto.email(),
                signUpRequestDto.password(),
                Provider.findByCode(signUpRequestDto.provider()));
        commandMemberPort.save(member);

        eventPublisher.publishEvent(new CreateUserAccountEvent(member.getId()));
    }

    @Override
    public Long signIn(SignInRequestDto signInRequestDto) {
        Member member = loadMemberPort.findByEmail(signInRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));
        return member.login(signInRequestDto.password());
    }

    @Override
    @Transactional(noRollbackFor = OAuthSignInException.class)
    public Long signIn(OAuthSignInRequestDto requestDto) {
        OAuthInfo oAuthInfo = loadOAuthInfoPort.loadOAuthInfo(requestDto);
        Provider provider = Provider.findByCode(requestDto.provider());
        Member member = loadMemberPort.loadMemberByEmailAndProvider(oAuthInfo.getEmail(), provider).orElseThrow(() -> {
            commandOAuthInfoPort.save(oAuthInfo);
            return new OAuthSignInException(oAuthInfo);
        });
        return member.getId();
    }

    @Override
    public GetTokenResponseDto getToken(Long memberId) {
        return loadTokenPort.loadToken(memberId);
    }
}
