package com.example.demo.service;

import com.example.demo.dto.DocumentEvent;
import com.example.demo.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String USER_TOPIC = "user-events";
    public static final String DOCUMENT_TOPIC = "document-events";
    public static final String USER_EVENT_OUTPUT_TOPIC = "user-events-processed";
    public static final String DOCUMENT_EVENT_OUTPUT_TOPIC = "document-events-processed";

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(UserEvent event) {
        log.info("Sending user event: {}", event);
        send(USER_TOPIC, event.getId(), event);
    }

    public void sendDocumentEvent(DocumentEvent event) {
        log.info("Sending document event: {}", event);
        send(DOCUMENT_TOPIC, event.getId(), event);
    }

    private void send(String topic, String key, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to topic '{}' with key '{}', offset: {}",
                        topic, key, result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message to topic '{}': {}", topic, ex.getMessage(), ex);
            }
        });
    }

    public void sendMessage(String topic, String key, Object message) {
        log.info("Sending message to topic '{}' with key '{}'", topic, key);
        send(topic, key, message);
    }
}
