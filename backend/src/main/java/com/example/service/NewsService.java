package com.example.service;

import com.example.dto.NewsResponse;
import com.example.mapper.NewsMapper;
import com.example.repository.NewsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NewsService {
    
    @Inject
    NewsRepository newsRepository;
    
    @Inject
    NewsMapper newsMapper;
    
    public List<NewsResponse> getNews(int page, int size) {
        return newsMapper.toResponseList(
            newsRepository.findAll()
                .page(page, size)
                .list()
        );
    }
    
    public Optional<NewsResponse> getNewsById(String id) {
        return newsRepository.findByIdOptional(new ObjectId(id))
            .map(newsMapper::toResponse);
    }
    
    public long count() {
        return newsRepository.count();
    }
}
