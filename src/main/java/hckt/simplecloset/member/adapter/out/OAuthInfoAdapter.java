package hckt.simplecloset.member.adapter.out;

import hckt.simplecloset.global.annotation.Adapter;
import hckt.simplecloset.member.adapter.out.communicate.OAuthInfoCommunicateAdapter;
import hckt.simplecloset.member.adapter.out.persistence.OAuthInfoRepository;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.application.port.out.CommandOAuthInfoPort;
import hckt.simplecloset.member.application.port.out.LoadOAuthInfoPort;
import hckt.simplecloset.member.domain.OAuthInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
class OAuthInfoAdapter implements CommandOAuthInfoPort, LoadOAuthInfoPort {
    private final OAuthInfoRepository oAuthInfoRepository;
    private final OAuthInfoCommunicateAdapter oAuthInfoCommunicateAdapter;

    @Override
    public void save(OAuthInfo oAuthInfo) {
        oAuthInfoRepository.save(oAuthInfo);
    }

    @Override
    public OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto) {
        return oAuthInfoCommunicateAdapter.loadOAuthInfo(requestDto);
    }

    @Override
    public Optional<OAuthInfo> loadOAuthInfo(String uid) {
        return oAuthInfoRepository.findOAuthInfoByUid(uid);
    }
}
