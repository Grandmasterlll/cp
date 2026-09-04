package com.example.demo.service;

import com.example.demo.document.DocumentEntity;
import com.example.demo.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EventPublisherService eventPublisher;

    public DocumentEntity findById(String id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    public List<DocumentEntity> findByAuthorId(String authorId) {
        return documentRepository.findByAuthorId(authorId);
    }

    public DocumentEntity create(DocumentEntity document) {
        DocumentEntity saved = documentRepository.save(document);
        eventPublisher.publishDocumentCreated(saved);
        return saved;
    }

    public DocumentEntity update(String id, DocumentEntity documentDetails) {
        DocumentEntity document = findById(id);
        document.setTitle(documentDetails.getTitle());
        document.setContent(documentDetails.getContent());
        document.setMetadata(documentDetails.getMetadata());
        DocumentEntity saved = documentRepository.save(document);
        eventPublisher.publishDocumentUpdated(saved);
        return saved;
    }

    public void delete(String id) {
        documentRepository.deleteById(id);
        eventPublisher.publishDocumentDeleted(id);
    }
}
