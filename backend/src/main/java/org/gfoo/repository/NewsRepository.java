package org.gfoo.repository;

import java.util.List;
import org.gfoo.entity.NewsDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NewsRepository implements PanacheMongoRepositoryBase<NewsDocument, String> {

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
