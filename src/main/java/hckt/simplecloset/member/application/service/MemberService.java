package hckt.simplecloset.member.application.service;

import hckt.simplecloset.global.annotation.UseCase;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignInRequestDto;
import hckt.simplecloset.member.application.dto.in.SignUpRequestDto;
import hckt.simplecloset.member.application.port.in.SignInUseCase;
import hckt.simplecloset.member.application.port.in.SignUpUseCase;
import hckt.simplecloset.member.application.port.out.CommandMemberPort;
import hckt.simplecloset.member.application.port.out.LoadMemberPort;
import hckt.simplecloset.member.domain.Member;
import hckt.simplecloset.member.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements SignInUseCase, SignUpUseCase {
    private final CommandMemberPort commandMemberPort;
    private final LoadMemberPort loadMemberPort;

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
    }

    @Override
    public Long signIn(SignInRequestDto signInRequestDto) {
        Member member = loadMemberPort.findByEmail(signInRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));
        return member.login(signInRequestDto.password());
    }

    @Override
    public Long signIn(OAuthSignInRequestDto oAuthSignInRequestDto) {
        return null;
    }
}
