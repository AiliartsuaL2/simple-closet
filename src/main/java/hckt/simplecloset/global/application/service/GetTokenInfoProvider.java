package hckt.simplecloset.global.application.service;

interface GetTokenInfoProvider {
    String getPayload(String token);
    boolean isValid(String token);
}
