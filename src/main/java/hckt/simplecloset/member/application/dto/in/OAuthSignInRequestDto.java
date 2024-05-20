package hckt.simplecloset.member.application.dto.in;

public record OAuthSignInRequestDto(String code, String provider) {
    // 로그인으로 들어온 RedirectUrl
    // /api/v1.0/oauth/{provider}
    public String getOAuthRedirectUri(String urlPrefix, String version) {
        return String.join("/", urlPrefix, "api", version, "oauth", provider);
    }
}
