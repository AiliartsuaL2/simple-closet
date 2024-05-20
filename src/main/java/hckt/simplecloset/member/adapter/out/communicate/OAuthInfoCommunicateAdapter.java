package hckt.simplecloset.member.adapter.out.communicate;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import hckt.simplecloset.global.annotation.CommunicateAdapter;
import hckt.simplecloset.global.domain.Provider;
import hckt.simplecloset.member.adapter.out.communicate.dto.OAuthCommunicateResponseDto;
import hckt.simplecloset.member.adapter.out.communicate.dto.OAuthToken;
import hckt.simplecloset.member.application.dto.in.OAuthSignInRequestDto;
import hckt.simplecloset.member.config.OAuth2ProviderProperties;
import hckt.simplecloset.member.config.ProviderInfo;
import hckt.simplecloset.member.domain.OAuthInfo;
import hckt.simplecloset.member.exception.CommunicationException;
import hckt.simplecloset.member.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@CommunicateAdapter
@Slf4j
public class OAuthInfoCommunicateAdapter {
    private final String url;
    private final String version;
    private final OAuth2ProviderProperties oAuth2ProviderProperties;

    public OAuthInfoCommunicateAdapter(
            @Value("${simple-closet.api.url}") String url,
            @Value("${oauth.version}") String version,
            OAuth2ProviderProperties oAuth2ProviderProperties) {
        this.url = url;
        this.version = version;
        this.oAuth2ProviderProperties = oAuth2ProviderProperties;
    }

    public OAuthInfo loadOAuthInfo(OAuthSignInRequestDto requestDto) {
        Provider provider = Provider.findByCode(requestDto.provider());
        String oAuthRedirectUri = requestDto.getOAuthRedirectUri(url, version);
        ProviderInfo providerInfo = oAuth2ProviderProperties.get(provider);
        OAuthCommunicateResponseDto responseDto = communicateWithOAuthServer(oAuthRedirectUri, providerInfo, requestDto.code());
        return new OAuthInfo(provider, responseDto.getEmail(), responseDto.getImage(), responseDto.getNickname());
    }

    OAuthCommunicateResponseDto communicateWithOAuthServer(String redirectUri, ProviderInfo providerInfo, String code) {
        String oAuthAccessToken = getOAuthAccessToken(redirectUri, providerInfo, code);
        return getUserInfo(providerInfo, oAuthAccessToken);
    }

    String getOAuthAccessToken(String redirectUri, ProviderInfo providerInfo, String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl(providerInfo.getTokenUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", providerInfo.getGrantType())
                        .queryParam("client_id", providerInfo.getClientId())
                        .queryParam("client_secret", providerInfo.getClientSecret())
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            log.error(clientResponse.bodyToMono(String.class).block());
                            return clientResponse.bodyToMono(String.class).map(CommunicationException::new);
                        })
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(1))
                .doOnError(e -> {
                    log.error(e.getMessage());
                    throw new CommunicationException(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());
                })
                .block();
        return convertByGson(response, OAuthToken.class).getAccessToken();
    }

    OAuthCommunicateResponseDto getUserInfo(ProviderInfo providerInfo, String accessToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl(providerInfo.getInfoUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                .build();
        String response = webClient.get()
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            log.error(clientResponse.bodyToMono(String.class).block());
                            return clientResponse.bodyToMono(String.class).map(CommunicationException::new);
                        })
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(1))
                .doOnError(e -> {
                    log.error(e.getMessage());
                    throw new CommunicationException(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());
                })
                .block();
        OAuthCommunicateResponseDto responseDto = convertByGson(response, providerInfo.getResponseType());
        responseDto.initAllFields();
        return responseDto;
    }

    <T> T convertByGson(String response, Class<? extends T> clazz) {
        if(!response.startsWith("{")) {
            response = "{\"" + response.replace("&", "\",\"").replace("=", "\":\"") + "\"}";
        }
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        try {
            return gson.fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            throw new CommunicationException(ErrorMessage.COMMUNICATE_EXCEPTION.getMessage());
        }
    }
}
