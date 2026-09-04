package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class UserEvent {

    private String id;
    private String username;
    private String email;
    private EventType eventType;
    private LocalDateTime timestamp;
    private Map<String, String> metadata;

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    public UserEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public UserEvent(String id, String username, String email, EventType eventType) {
        this();
        this.id = id;
        this.username = username;
        this.email = email;
        this.eventType = eventType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "UserEvent{id='" + id + "', username='" + username + "', type=" + eventType + "}";
    }
}
