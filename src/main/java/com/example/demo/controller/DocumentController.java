package com.example.demo.controller;

import com.example.demo.document.DocumentEntity;
import com.example.demo.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocumentById(@PathVariable String id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByAuthor(@PathVariable String authorId) {
        return ResponseEntity.ok(documentService.findByAuthorId(authorId));
    }

    @PostMapping
    public ResponseEntity<DocumentEntity> createDocument(@RequestBody DocumentEntity document) {
        return ResponseEntity.ok(documentService.create(document));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(@PathVariable String id, @RequestBody DocumentEntity document) {
        return ResponseEntity.ok(documentService.update(id, document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
