package org.gfoo.service;

import java.util.List;
import java.util.Optional;
import org.gfoo.dto.NewsResponse;
import org.gfoo.mapper.NewsMapper;
import org.gfoo.repository.NewsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NewsService {

    @Inject
    NewsRepository newsRepository;

    @Inject
    NewsMapper newsMapper;

    public List<NewsResponse> getNews(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        return newsMapper.toResponseList(
                newsRepository.findAll()
                        .page(page, size)
                        .list());
    }

    public Optional<NewsResponse> getNewsById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return newsRepository.findByIdOptional(id)
                .map(newsMapper::toResponse);
    }

    public long count() {
        return newsRepository.count();
    }
}
