package hckt.simplecloset.member.adapter.out.communicate.dto;

public class GoogleResponseDto extends OAuthCommunicateResponseDto {
    private String id;
    private String picture;
    private String name;

    @Override
    public void initAllFields() {
        init(super.getEmail(), this.picture, this.name);
    }
}