package hckt.simplecloset.global.domain;

import hckt.simplecloset.global.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum RoleType implements GrantedAuthority {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String authority;

    public static RoleType findByAuthority(String roleType) {
        for (RoleType value : values()) {
            if (value.authority.equals(roleType)) {
                return value;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_ROLE_TYPE.getMessage());
    }
}
