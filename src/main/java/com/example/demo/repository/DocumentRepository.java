package com.example.demo.repository;

import com.example.demo.document.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {

    List<DocumentEntity> findByAuthorId(String authorId);
}
