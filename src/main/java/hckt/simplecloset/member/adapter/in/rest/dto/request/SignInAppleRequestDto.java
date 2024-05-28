package hckt.simplecloset.member.adapter.in.rest.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInAppleRequestDto {
    private String code;
    private String id_token;
}