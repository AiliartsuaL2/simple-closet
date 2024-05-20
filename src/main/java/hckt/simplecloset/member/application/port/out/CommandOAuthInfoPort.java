package hckt.simplecloset.member.application.port.out;

import hckt.simplecloset.member.domain.OAuthInfo;

public interface CommandOAuthInfoPort {
    void save(OAuthInfo oAuthInfo);
}
