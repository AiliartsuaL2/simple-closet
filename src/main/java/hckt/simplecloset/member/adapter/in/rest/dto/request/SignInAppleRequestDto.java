package hckt.simplecloset.member.adapter.in.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SignInAppleRequestDto {
    private String code;
    private String id_token;
}