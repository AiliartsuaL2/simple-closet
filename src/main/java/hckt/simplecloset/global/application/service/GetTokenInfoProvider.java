package hckt.simplecloset.global.application.service;

public interface GetTokenInfoProvider {
    String getPayload(String token);
    boolean isValid(String token);
}
