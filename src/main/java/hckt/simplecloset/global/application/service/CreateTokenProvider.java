package hckt.simplecloset.global.application.service;

interface CreateTokenProvider {
    String createAccessToken(final String payload);
    String createRefreshToken(final String payload);
}
