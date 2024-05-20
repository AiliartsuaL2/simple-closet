package hckt.simplecloset.member.exception;

import hckt.simplecloset.member.domain.OAuthInfo;
import lombok.Getter;

@Getter
public class OAuthSignInException extends RuntimeException {
    private final String uid;
    public OAuthSignInException(OAuthInfo oAuthInfo) {
        super(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        this.uid = oAuthInfo.getUid();
    }
}
