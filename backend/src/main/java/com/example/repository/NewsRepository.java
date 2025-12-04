package com.example.repository;

import com.example.entity.NewsDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NewsRepository implements PanacheMongoRepository<NewsDocument> {
    
    public List<NewsDocument> findByLanguage(String language) {
        return list("language", language);
    }
    
    public List<NewsDocument> findByStatus(String status) {
        return list("status", status);
    }
    
    public List<NewsDocument> findUsableNews() {
        return list("status", "usable");
    }
}
