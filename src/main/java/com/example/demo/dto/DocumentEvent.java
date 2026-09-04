package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentEvent {

    private String id;
    private String title;
    private String authorId;
    private DocumentEventType eventType;
    private LocalDateTime timestamp;
    private String content;
    private List<String> tags;

    public enum DocumentEventType {
        CREATED, UPDATED, DELETED
    }

    public DocumentEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public DocumentEvent(String id, String title, String authorId, DocumentEventType eventType) {
        this();
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.eventType = eventType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public DocumentEventType getEventType() { return eventType; }
    public void setEventType(DocumentEventType eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    @Override
    public String toString() {
        return "DocumentEvent{id='" + id + "', title='" + title + "', type=" + eventType + "}";
    }
}
