package hckt.simplecloset.global.domain;

import hckt.simplecloset.global.domain.converter.CodeValue;
import hckt.simplecloset.global.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum RoleType implements GrantedAuthority, CodeValue {
    ROLE_ADMIN("ROLE_ADMIN", "admin", "어드민"),
    ROLE_USER("ROLE_USER", "user", "일반 회원");

    private final String authority;
    private final String code;
    private final String value;

    public static RoleType findByAuthority(String roleType) {
        for (RoleType value : values()) {
            if (value.authority.equals(roleType)) {
                return value;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_ROLE_TYPE.getMessage());
    }
}
