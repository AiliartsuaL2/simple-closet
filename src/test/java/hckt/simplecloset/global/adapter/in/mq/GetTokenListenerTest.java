package hckt.simplecloset.global.adapter.in.mq;

import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.util.Constants;
import hckt.simplecloset.global.application.port.in.CreateTokenUseCase;
import hckt.simplecloset.global.config.AwsConfig;
import hckt.simplecloset.global.domain.Token;
import hckt.simplecloset.global.exception.ErrorMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AwsConfig.class)
class GetTokenListenerTest {
    private static final String QUEUE_NAME = "simpleClosetMessageTestQueue";
    @Autowired
    AmazonSQSResponder responder;
    private static String queueUrl;

    @BeforeAll
    public static void beforeAll(@Autowired AmazonSQSResponder responder) {
        queueUrl = responder.getAmazonSQS().createQueue(CreateQueueRequest.builder().queueName(QUEUE_NAME).build()).queueUrl();
    }

    @AfterAll
    public static void afterAll(@Autowired AmazonSQSResponder responder) {
        responder.getAmazonSQS().deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build());
    }

    @Nested
    @DisplayName("로그인 이벤트 핸들링 테스트")
    class HandleLoginRequest {
        private static final Long MEMBER_ID = 0L;
        private static final String ACCESS_TOKEN = "accessToken : abcdeftgsad.asdfxvcv.xczvzxcv";
        private static final String REFRESH_TOKEN = "refreshToken : sdafv.cvcxxasdf.sdgwetw";

        CreateTokenUseCase createTokenUseCase = mock(CreateTokenUseCase.class);
        GetTokenListener loginMemberMessageListenerMock = new GetTokenListener(responder, createTokenUseCase);
        Token token = new Token(ACCESS_TOKEN, REFRESH_TOKEN);
        Message message = Message.builder()
                .body(String.valueOf(MEMBER_ID))
                .messageAttributes(Map.of(Constants.RESPONSE_QUEUE_URL_ATTRIBUTE_NAME,
                        MessageAttributeValue.builder()
                                .stringValue(queueUrl)
                                .build()))
                .build();

        @Test
        @DisplayName("메세지 수신시 응답에 ACCESS_TOKEN과 REFRESH_TOKEN이 존재한다.")
        void test1() {
            //given
            when(createTokenUseCase.create(MEMBER_ID))
                    .thenReturn(token);

            //when
            loginMemberMessageListenerMock.messageListener(message);
            String response = receiveMessage();

            //then
            assertThat(response).contains(ACCESS_TOKEN);
            assertThat(response).contains(REFRESH_TOKEN);
        }

        @Test
        @DisplayName("메세지 데이터가 회원 ID가 아닌경우 에러 메세지 응답.")
        void test2() {
            //given
            String malformedToken = "malformedToken";
            Message message = Message.builder()
                    .body(malformedToken)
                    .messageAttributes(Map.of(Constants.RESPONSE_QUEUE_URL_ATTRIBUTE_NAME,
                            MessageAttributeValue.builder()
                                    .stringValue(queueUrl)
                                    .build()))
                    .build();

            //when
            loginMemberMessageListenerMock.messageListener(message);
            String response = receiveMessage();

            // then
            assertThat(response).isEqualTo(ErrorMessage.INVALID_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("회원 ID가 null인 경우(tokenCreate에서 IAE가 발생하는경우) 예외 메세지를 응답한다.")
        void test3() {
            //given
            when(createTokenUseCase.create(MEMBER_ID))
                    .thenThrow(new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage()));

            //when
            loginMemberMessageListenerMock.messageListener(message);
            String response = receiveMessage();

            // then
            assertThat(response).isEqualTo(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("가입되지 않은 회원인 경우(tokenCreate에서 ISE가 발생하는경우) 예외 메세지를 응답한다.")
        void test4() {
            //given
            when(createTokenUseCase.create(MEMBER_ID))
                    .thenThrow(new IllegalStateException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));

            //when
            loginMemberMessageListenerMock.messageListener(message);
            String response = receiveMessage();

            // then
            assertThat(response).isEqualTo(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        private String receiveMessage() {
            ReceiveMessageResponse receiveMessageResponse = responder.getAmazonSQS()
                    .receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(1).build());
            return receiveMessageResponse.messages().get(0).body();
        }
    }

}