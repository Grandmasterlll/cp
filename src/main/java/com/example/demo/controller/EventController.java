package com.example.demo.controller;

import com.example.demo.dto.DocumentEvent;
import com.example.demo.dto.UserEvent;
import com.example.demo.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final KafkaProducerService kafkaProducerService;

    public EventController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> publishUserEvent(@RequestBody UserEvent event) {
        kafkaProducerService.sendUserEvent(event);
        return ResponseEntity.ok(Map.of(
                "status", "published",
                "topic", KafkaProducerService.USER_TOPIC,
                "eventId", event.getId() != null ? event.getId() : "unknown"
        ));
    }

    @PostMapping("/document")
    public ResponseEntity<Map<String, String>> publishDocumentEvent(@RequestBody DocumentEvent event) {
        kafkaProducerService.sendDocumentEvent(event);
        return ResponseEntity.ok(Map.of(
                "status", "published",
                "topic", KafkaProducerService.DOCUMENT_TOPIC,
                "eventId", event.getId() != null ? event.getId() : "unknown"
        ));
    }
}
