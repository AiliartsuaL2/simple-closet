package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.application.port.in.CreateUserAccountUseCase;
import hckt.simplecloset.global.application.port.out.CommandUserAccountPort;
import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.domain.UserAccount;
import hckt.simplecloset.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserAccountService implements UserDetailsService, CreateUserAccountUseCase {

    private final LoadUserAccountPort loadUserAccountPort;
    private final CommandUserAccountPort commandUserAccountPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserAccountPort.findUserAccountByMemberId(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage()));
    }

    @Override
    @Transactional
    public void create(UserAccount userAccount) {
        commandUserAccountPort.create(userAccount);
    }
}
