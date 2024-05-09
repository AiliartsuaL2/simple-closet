package hckt.simplecloset.global.application.port.in;
import org.springframework.security.core.Authentication;

public interface GetAuthenticationUseCase {
    Authentication getAuthentication(String accessToken);
}
