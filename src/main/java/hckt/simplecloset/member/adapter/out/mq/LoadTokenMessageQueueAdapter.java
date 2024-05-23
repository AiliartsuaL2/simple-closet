package hckt.simplecloset.member.adapter.out.mq;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import hckt.simplecloset.global.annotation.MessageQueue;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.application.port.out.LoadTokenPort;
import hckt.simplecloset.member.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MessageQueue
@Slf4j
public class LoadTokenMessageQueueAdapter implements LoadTokenPort {
    private final AmazonSQSRequester sqsRequester;
    private final String requestQueueUrl;

    public LoadTokenMessageQueueAdapter(@Value("${sqs.signin.request}") String requestQueueUrl, AmazonSQSRequester sqsRequester) {
        this.sqsRequester = sqsRequester;
        this.requestQueueUrl = requestQueueUrl;
    }

    @Override
    public GetTokenResponseDto loadToken(Long memberId) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(String.valueOf(memberId))
                .build();
        try {
            Message reply = sqsRequester.sendMessageAndGetResponse(request,2, TimeUnit.SECONDS);
            JSONObject json = new JSONObject(reply.body());
            String accessToken = (String) json.get("accessToken");
            String refreshToken = (String) json.get("refreshToken");
            return new GetTokenResponseDto(accessToken, refreshToken);
        } catch (SdkClientException | TimeoutException | JSONException e) {
            log.error(e.getMessage());
            throw new MessagingException(ErrorMessage.AUTH_COMMUNICATION_EXCEPTION.getMessage());
        }
    }
}
