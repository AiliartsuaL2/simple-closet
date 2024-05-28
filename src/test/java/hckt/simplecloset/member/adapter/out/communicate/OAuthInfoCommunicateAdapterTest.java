package hckt.simplecloset.member.adapter.out.communicate;

import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.adapter.in.rest.dto.request.SignInAppleRequestDto;
import hckt.simplecloset.member.adapter.out.communicate.dto.GoogleResponseDto;
import hckt.simplecloset.member.adapter.out.communicate.dto.KakaoResponseDto;
import hckt.simplecloset.member.adapter.out.communicate.dto.OAuthCommunicateResponseDto;
import hckt.simplecloset.member.config.OAuth2ProviderProperties;
import hckt.simplecloset.member.config.ProviderInfo;
import hckt.simplecloset.member.domain.OAuthInfo;
import hckt.simplecloset.member.exception.CommunicationException;
import hckt.simplecloset.member.exception.ErrorMessage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class OAuthInfoCommunicateAdapterTest {
    private static final String EMAIL = "email";
    private static final String PICTURE = "picture";
    private static final String NICKNAME = "nickname";
    private static final String URL = "url";
    private static final String VERSION = "version";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String LOGOUT_URL = "logoutUrl";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REDIRECT_URI = "redirectUri";

    OAuth2ProviderProperties oAuth2ProviderProperties = mock(OAuth2ProviderProperties.class);
    OAuthInfoCommunicateAdapter oAuthInfoCommunicateAdapter = new OAuthInfoCommunicateAdapter(URL, VERSION, oAuth2ProviderProperties);

    MockWebServer mockWebServer;
    String mockWebServerUrl ;

    @BeforeEach
    void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServerUrl = mockWebServer.url("").toString();
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("OAuth 서버 액세스 토큰 조회 통신 테스트")
    class GetOAuthAccessToken {
        @Test
        @DisplayName("OAuth Server 400 응답시 예외 발생")
        void test1() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, mockWebServerUrl,
                    LOGOUT_URL);
            String code = "code";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(400)
                    .setBody("errorMessage"));

            //when & then
            assertThatThrownBy(() -> oAuthInfoCommunicateAdapter.getOAuthAccessToken(REDIRECT_URI, providerInfo, code))
                    .isInstanceOf(CommunicationException.class)
                    .hasMessage(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("OAuth Server 200 응답시, 액세스 토큰 반환")
        void test2() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, mockWebServerUrl,
                    LOGOUT_URL);

            String responseJson = "{"
                    + "\"access_token\":\"" + ACCESS_TOKEN + "\""
                    + "}";
            String code = "code";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson));

            //when
            String oAuthAccessToken = oAuthInfoCommunicateAdapter.getOAuthAccessToken(REDIRECT_URI, providerInfo, code);

            //then
            assertThat(oAuthAccessToken).isEqualTo(ACCESS_TOKEN);
        }
    }

    @Nested
    @DisplayName("OAuth 서버 회원 정보 조회 통신 테스트")
    class GetOAuthInfo {
        @Test
        @DisplayName("OAuth Server 400 응답시 예외 발생")
        void test1() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, mockWebServerUrl,
                    LOGOUT_URL);

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(400)
                    .setBody("errorMessage"));

            //when & then
            assertThatThrownBy(() -> oAuthInfoCommunicateAdapter.getUserInfo(providerInfo, ACCESS_TOKEN))
                    .isInstanceOf(CommunicationException.class)
                    .hasMessage(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());

        }

        @Test
        @DisplayName("구글 로그인으로 OAuth Server 200 응답시, 회원 정보 반환")
        void test2() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, mockWebServerUrl,
                    LOGOUT_URL);
            providerInfo.setResponseType(GoogleResponseDto.class);

            String responseJson = "{"
                    + "\"email\":\"" + EMAIL + "\","
                    + "\"picture\":\"" + PICTURE + "\","
                    + "\"name\":\"" + NICKNAME + "\""
                    + "}";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson));

            //when
            OAuthCommunicateResponseDto userInfo = oAuthInfoCommunicateAdapter.getUserInfo(providerInfo, ACCESS_TOKEN);

            //then
            assertThat(userInfo.getEmail()).isEqualTo(EMAIL);
            assertThat(userInfo.getImage()).isEqualTo(PICTURE);
            assertThat(userInfo.getNickname()).isEqualTo(NICKNAME);
        }

        @Test
        @DisplayName("카카오 로그인으로 OAuth Server 200 응답시, 회원 정보 반환")
        void test3() {
            //given
            ProviderInfo providerInfo = new ProviderInfo(CLIENT_ID, CLIENT_SECRET, mockWebServerUrl, mockWebServerUrl,
                    LOGOUT_URL);
            providerInfo.setResponseType(KakaoResponseDto.class);

            String responseJson = "{"
                    + "\"properties\":{"+
                    "\"nickname\":\""+NICKNAME+"\","+
                    "\"profile_image\":\""+PICTURE+"\""
                    +       "},"
                    + "\"kakao_account\":{"+
                    "\"email\":\""+EMAIL+"\""
                    +       "}"
                    + "}";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson));

            //when
            OAuthCommunicateResponseDto userInfo = oAuthInfoCommunicateAdapter.getUserInfo(providerInfo, ACCESS_TOKEN);

            //then
            assertThat(userInfo.getEmail()).isEqualTo(EMAIL);
            assertThat(userInfo.getImage()).isEqualTo(PICTURE);
            assertThat(userInfo.getNickname()).isEqualTo(NICKNAME);
        }

        @Test
        @DisplayName("애플 로그인으로 OAuth Server 200 응답시, 회원 정보 반환")
        void test4() {
            // given
            String token = "eyJraWQiOiJweWFSUXBBYm5ZIiwiYWxnIjoiUlMyNTYifQ" +
                    ".eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiTXlDbG9zZXQubGVlbGVlbGVlc29uZy5jb20iLCJleHAiOjE3MTY5NjU2ODEsImlhdCI6MTcxNjg3OTI4MSwic3ViIjoiMDAwODg3LjhkNTFjNTY2NjhjNTQ3MGFhY2Y3ZGNmMTRhYzVmOThlLjA2NTQiLCJjX2hhc2giOiJMclR2QW9vamwzUzVjbHEzQmZRYUtBIiwiZW1haWwiOiJqdWhvNzRAbmF2ZXIuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF1dGhfdGltZSI6MTcxNjg3OTI4MSwibm9uY2Vfc3VwcG9ydGVkIjp0cnVlfQ" +
                    ".UrD8MB75Xac6HITxtKtibvaJt6FGM2ZE2DnJ31vkxoeuJK-U7tTsfOReE9VXU5JUWj-bIkZEU3J9C8DSHNEwGWLBv1UsTNLu-HmDuh9OELhjtavcJP2IIOKLi-Xiqu53wXmD73VXHsdv1XeM9l2U3L-KuUSSXU60BU2AS8vKENASaT9ee1VXRaqtr67P67jrJe0VWeVr8afnoyAToIPZNM-whiexXjQlsRiMvFUqNHz4UVYrbAzMcUSifAb_WSeGw-dVuhrxIohtDH5sWXfTpAEE8cSN3A3i6P3FtRvVkrmCU4bAm2f783QWF43XEGs1H9wFSoDpdLeRcI7DsJ-Ldw";
            SignInAppleRequestDto requestDto = mock(SignInAppleRequestDto.class);
            String expectedEmail = "juho74@naver.com";

            when(requestDto.getId_token())
                    .thenReturn(token);
            ProviderInfo providerInfo = mock(ProviderInfo.class);

            when(providerInfo.getTokenUrl())
                    .thenReturn("https://appleid.apple.com/auth/keys");
            when(oAuth2ProviderProperties.get(Provider.APPLE))
                    .thenReturn(providerInfo);

            // when
            OAuthInfo oAuthInfo = oAuthInfoCommunicateAdapter.loadOAuthInfo(requestDto);

            // then
            Assertions.assertThat(oAuthInfo.getEmail()).isEqualTo(expectedEmail);
        }
    }
}
