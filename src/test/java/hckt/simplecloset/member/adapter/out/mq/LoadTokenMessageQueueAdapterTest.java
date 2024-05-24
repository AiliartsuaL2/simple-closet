package hckt.simplecloset.member.adapter.out.mq;

import com.amazonaws.services.sqs.AmazonSQSRequester;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.MessageContent;
import com.amazonaws.services.sqs.util.Constants;
import com.google.gson.Gson;
import hckt.simplecloset.global.config.AwsConfig;
import hckt.simplecloset.global.domain.Token;
import hckt.simplecloset.member.application.dto.out.GetTokenResponseDto;
import hckt.simplecloset.member.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessagingException;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@SpringBootTest(classes = AwsConfig.class)
class LoadTokenMessageQueueAdapterTest {
    private static final String QUEUE_NAME = "simpleClosetMessageTestQueue";
    @Autowired
    AmazonSQSRequester requester;
    @Autowired
    AmazonSQSResponder responder;
    private static String queueUrl;

    @BeforeAll
    public static void beforeAll(@Autowired AmazonSQSRequester requester) {
        queueUrl = requester.getAmazonSQS()
                .createQueue(CreateQueueRequest
                        .builder()
                        .queueName(QUEUE_NAME)
                        .build())
                .queueUrl();
    }

    @Nested
    @DisplayName("로그인 이벤트 발송 테스트")
    class SignIn {
        private static final Long MEMBER_ID = 0L;
        private static final String ACCESS_TOKEN = "accessToken";
        private static final String REFRESH_TOKEN = "refreshToken";

        LoadTokenMessageQueueAdapter loadTokenMqAdapter = new LoadTokenMessageQueueAdapter(queueUrl, requester);
        Message message = Message.builder()
                .body(new Gson().toJson(MEMBER_ID))
                .messageAttributes(Map.of(Constants.RESPONSE_QUEUE_URL_ATTRIBUTE_NAME,
                        MessageAttributeValue.builder()
                                .stringValue(queueUrl)
                                .build()))
                .build();

        @Test
        @DisplayName("로그인시 token 객체를 받아온다.")
        void test1() {
            //given
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();
            Token token = new Token(ACCESS_TOKEN, REFRESH_TOKEN);

            executorService.submit(() -> {
                // 임의 전송
                responder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(new Gson().toJson(token)));
            });

            executorService2.submit(() -> {
                // when
                GetTokenResponseDto response = loadTokenMqAdapter.loadToken(MEMBER_ID);

                //then
                assertThat(response.accessToken()).isEqualTo(token.getAccessToken());
                assertThat(response.refreshToken()).isEqualTo(token.getRefreshToken());
            });

            executorService.shutdown();
            executorService2.shutdown();
        }

        @Test
        @DisplayName("통신 간 Timeout Exception 발생 시 MessagingException이 발생한다.")
        void test2() throws TimeoutException {
            //given & when & then
            assertThatThrownBy(() -> loadTokenMqAdapter.loadToken(MEMBER_ID))
                    .isInstanceOf(MessagingException.class)
                    .hasMessage(ErrorMessage.AUTH_COMMUNICATION_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("응답받은 token이 GetTokenResponseDto 타입이 아닌 경우 MessagingException이 발생한다.")
        void test3() throws TimeoutException {
            //given
            String invalidToken = "invalidToken";
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();

            executorService.submit(() -> {
                responder.sendResponseMessage(MessageContent.fromMessage(message),new MessageContent(invalidToken));
            });

            //when & then
            executorService2.submit(() -> {
                assertThatThrownBy(() -> loadTokenMqAdapter.loadToken(MEMBER_ID))
                        .isInstanceOf(MessagingException.class)
                        .hasMessage(ErrorMessage.AUTH_COMMUNICATION_EXCEPTION.getMessage());
            });

            executorService.shutdown();
            executorService2.shutdown();
        }
    }
}
