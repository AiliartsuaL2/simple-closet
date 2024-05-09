package hckt.simplecloset.global.application.port.in;

import hckt.simplecloset.global.domain.Token;

public interface CreateTokenUseCase {
    Token create(Long userAccountId);
}
