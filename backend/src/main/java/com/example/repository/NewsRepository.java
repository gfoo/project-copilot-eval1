package com.example.repository;

import com.example.entity.NewsDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NewsRepository implements PanacheMongoRepository<NewsDocument> {
    
    public List<NewsDocument> findByLanguage(String language) {
        if (language == null) {
            throw new IllegalArgumentException("Language cannot be null");
        }
        return list("language", language);
    }
    
    public List<NewsDocument> findByStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return list("status", status);
    }
    
    public List<NewsDocument> findUsableNews() {
        return list("status", "usable");
    }
}
