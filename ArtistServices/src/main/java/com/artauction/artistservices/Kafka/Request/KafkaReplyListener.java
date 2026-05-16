package com.artauction.artistservices.Kafka.Request;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class KafkaReplyListener {
    // Attributes
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Listen to check admin reply
    @Bean
    public Consumer<Message<Boolean>> checkAdminReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckAdminReply(correlationId, message.getPayload());
        };
    }
}
