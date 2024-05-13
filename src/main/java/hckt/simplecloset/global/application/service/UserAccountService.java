package hckt.simplecloset.global.application.service;

import hckt.simplecloset.global.application.port.out.LoadUserAccountPort;
import hckt.simplecloset.global.exception.ErrorMessage;
import hckt.simplecloset.global.exception.NotRegisteredUserAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserAccountService implements UserDetailsService {

    private final LoadUserAccountPort loadUserAccountPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserAccountPort.findById(Long.parseLong(username))
                .orElseThrow(() -> new NotRegisteredUserAccountException(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage()));
    }
}
