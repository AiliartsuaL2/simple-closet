package hckt.simplecloset.member.adapter.out.communicate.dto;

import lombok.Getter;

@Getter
public abstract class OAuthCommunicateResponseDto {
    private String email;
    private String image;
    private String nickname;

    protected void init(String email, String image, String nickname) {
        this.email = email;
        this.image = image;
        this.nickname = nickname;
    }

    public abstract void initAllFields();
}
