package hckt.simplecloset.global.application.port.in;

public interface RenewAccessTokenUseCase {
    String renewAccessToken(String refreshToken);
}
