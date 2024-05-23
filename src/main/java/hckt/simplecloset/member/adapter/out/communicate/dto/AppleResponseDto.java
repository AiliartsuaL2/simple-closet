package hckt.simplecloset.member.adapter.out.communicate.dto;

public class AppleResponseDto extends OAuthCommunicateResponseDto {
    private String id;
    private String avatarUrl;
    private String login;

    @Override
    public void initAllFields() {
        init(super.getEmail(), this.avatarUrl, this.login);
    }
}
