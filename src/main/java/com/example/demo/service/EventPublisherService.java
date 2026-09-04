package com.example.demo.service;

import com.example.demo.dto.DocumentEvent;
import com.example.demo.dto.UserEvent;
import com.example.demo.document.DocumentEntity;
import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final KafkaProducerService kafkaProducerService;

    public void publishUserCreated(User user) {
        UserEvent event = new UserEvent(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                UserEvent.EventType.CREATED
        );
        kafkaProducerService.sendUserEvent(event);
        log.info("Published user created event: {}", user.getUsername());
    }

    public void publishUserUpdated(User user) {
        UserEvent event = new UserEvent(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                UserEvent.EventType.UPDATED
        );
        kafkaProducerService.sendUserEvent(event);
        log.info("Published user updated event: {}", user.getUsername());
    }

    public void publishUserDeleted(Long userId) {
        UserEvent event = new UserEvent(
                userId.toString(),
                "deleted_user",
                null,
                UserEvent.EventType.DELETED
        );
        kafkaProducerService.sendUserEvent(event);
        log.info("Published user deleted event: id={}", userId);
    }

    public void publishDocumentCreated(DocumentEntity document) {
        DocumentEvent event = new DocumentEvent(
                document.getId(),
                document.getTitle(),
                document.getAuthorId(),
                DocumentEvent.DocumentEventType.CREATED
        );
        event.setContent(document.getContent());
        event.setTags((java.util.List<String>) document.getMetadata().get("tags"));
        kafkaProducerService.sendDocumentEvent(event);
        log.info("Published document created event: {}", document.getTitle());
    }

    public void publishDocumentUpdated(DocumentEntity document) {
        DocumentEvent event = new DocumentEvent(
                document.getId(),
                document.getTitle(),
                document.getAuthorId(),
                DocumentEvent.DocumentEventType.UPDATED
        );
        event.setContent(document.getContent());
        event.setTags((java.util.List<String>) document.getMetadata().get("tags"));
        kafkaProducerService.sendDocumentEvent(event);
        log.info("Published document updated event: {}", document.getTitle());
    }

    public void publishDocumentDeleted(String documentId) {
        DocumentEvent event = new DocumentEvent(
                documentId,
                "deleted_document",
                null,
                DocumentEvent.DocumentEventType.DELETED
        );
        kafkaProducerService.sendDocumentEvent(event);
        log.info("Published document deleted event: id={}", documentId);
    }
}