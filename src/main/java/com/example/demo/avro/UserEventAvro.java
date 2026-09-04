package com.example.demo.avro;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Generated Avro class for UserEvent schema
 */
public class UserEventAvro {

    private String id;
    private String username;
    private String email;
    private String eventType;
    private long timestamp;
    private Map<String, String> metadata;

    public UserEventAvro() {}

    public UserEventAvro(String id, String username, String email, String eventType, long timestamp, Map<String, String> metadata) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "UserEventAvro{id='" + id + "', username='" + username + "', eventType='" + eventType + "'}";
    }
}
