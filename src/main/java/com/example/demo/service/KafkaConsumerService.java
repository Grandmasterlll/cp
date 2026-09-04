package com.example.demo.service;

import com.example.demo.dto.DocumentEvent;
import com.example.demo.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    public static final String USER_TOPIC = "user-events";
    public static final String DOCUMENT_TOPIC = "document-events";
    public static final String USER_GROUP = "user-event-consumer-group";
    public static final String DOCUMENT_GROUP = "document-event-consumer-group";

    @KafkaListener(topics = USER_TOPIC, groupId = USER_GROUP)
    public void listenUserEvent(UserEvent event) {
        log.info("Received user event: {}", event);
        processUserEvent(event);
    }

    @KafkaListener(topics = DOCUMENT_TOPIC, groupId = DOCUMENT_GROUP)
    public void listenDocumentEvent(DocumentEvent event) {
        log.info("Received document event: {}", event);
        processDocumentEvent(event);
    }

    private void processUserEvent(UserEvent event) {
        log.info("Processing user event: type={}, user={}", event.getEventType(), event.getUsername());
        
        switch (event.getEventType()) {
            case CREATED -> log.info("New user registered: {}", event.getUsername());
            case UPDATED -> log.info("User updated: {}", event.getUsername());
            case DELETED -> log.info("User deleted: {}", event.getUsername());
        }
    }

    private void processDocumentEvent(DocumentEvent event) {
        log.info("Processing document event: type={}, doc={}", event.getEventType(), event.getTitle());
        
        switch (event.getEventType()) {
            case CREATED -> log.info("New document created: {}", event.getTitle());
            case UPDATED -> log.info("Document updated: {}", event.getTitle());
            case DELETED -> log.info("Document deleted: {}", event.getTitle());
        }
    }
}
