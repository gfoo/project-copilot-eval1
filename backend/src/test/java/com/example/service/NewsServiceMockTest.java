package com.example.service;

import com.example.dto.NewsResponse;
import com.example.entity.NewsDocument;
import com.example.mapper.NewsMapper;
import com.example.repository.NewsRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@QuarkusTest
public class NewsServiceMockTest {
    
    @InjectMock
    NewsRepository newsRepository;
    
    @InjectMock
    NewsMapper newsMapper;
    
    @Inject
    NewsService newsService;
    
    @Test
    void testGetNews_shouldReturnMappedList() {
        // Given
        int page = 0;
        int size = 10;
        
        // Create mock documents
        NewsDocument doc1 = new NewsDocument();
        doc1.id = new ObjectId();
        doc1.title = "News 1";
        
        NewsDocument doc2 = new NewsDocument();
        doc2.id = new ObjectId();
        doc2.title = "News 2";
        
        List<NewsDocument> documents = Arrays.asList(doc1, doc2);
        
        // Create mock responses
        NewsResponse response1 = new NewsResponse(
            doc1.id.toString(), "News 1", "news", "Catchline 1", "en", "usable"
        );
        NewsResponse response2 = new NewsResponse(
            doc2.id.toString(), "News 2", "news", "Catchline 2", "en", "usable"
        );
        List<NewsResponse> expectedResponses = Arrays.asList(response1, response2);
        
        // Mock the repository's findAll().page(page, size).list() chain
        // We need to mock PanacheQuery returned by findAll()
        io.quarkus.mongodb.panache.PanacheQuery<NewsDocument> mockQuery = 
            Mockito.mock(io.quarkus.mongodb.panache.PanacheQuery.class);
        
        when(newsRepository.findAll()).thenReturn(mockQuery);
        when(mockQuery.page(page, size)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(documents);
        
        // Mock mapper behavior
        when(newsMapper.toResponseList(documents)).thenReturn(expectedResponses);
        
        // When
        List<NewsResponse> result = newsService.getNews(page, size);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses, result);
        
        // Verify interactions
        verify(newsRepository).findAll();
        verify(newsMapper).toResponseList(documents);
    }
    
    @Test
    void testGetNewsById_whenExists_shouldReturnNews() {
        // Given
        String id = "507f1f77bcf86cd799439011";
        ObjectId objectId = new ObjectId(id);
        
        NewsDocument document = new NewsDocument();
        document.id = objectId;
        document.title = "Existing News";
        document.catchLine = "This exists";
        document.isEvent = false;
        
        NewsResponse expectedResponse = new NewsResponse(
            id, "Existing News", "news", "This exists", "en", "usable"
        );
        
        // Mock repository behavior
        when(newsRepository.findByIdOptional(objectId)).thenReturn(Optional.of(document));
        
        // Mock mapper behavior
        when(newsMapper.toResponse(document)).thenReturn(expectedResponse);
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(id);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
        assertEquals("Existing News", result.get().title());
        assertEquals("news", result.get().type());
        
        // Verify interactions
        verify(newsRepository).findByIdOptional(objectId);
        verify(newsMapper).toResponse(document);
    }
    
    @Test
    void testGetNewsById_whenNotExists_shouldReturnEmpty() {
        // Given
        String id = "507f1f77bcf86cd799439011";
        ObjectId objectId = new ObjectId(id);
        
        // Mock repository behavior - document not found
        when(newsRepository.findByIdOptional(objectId)).thenReturn(Optional.empty());
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(id);
        
        // Then
        assertFalse(result.isPresent());
        
        // Verify interactions
        verify(newsRepository).findByIdOptional(objectId);
        verify(newsMapper, never()).toResponse(any());
    }
    
    @Test
    void testCount_shouldReturnRepositoryCount() {
        // Given
        long expectedCount = 42L;
        
        // Mock repository behavior
        when(newsRepository.count()).thenReturn(expectedCount);
        
        // When
        long result = newsService.count();
        
        // Then
        assertEquals(expectedCount, result);
        
        // Verify interactions
        verify(newsRepository).count();
    }
    
    // Additional edge case tests
    
    @Test
    void testGetNews_withEmptyList_shouldReturnEmptyList() {
        // Given
        int page = 0;
        int size = 10;
        List<NewsDocument> emptyDocuments = new ArrayList<>();
        List<NewsResponse> emptyResponses = new ArrayList<>();
        
        // Mock the repository's findAll().page(page, size).list() chain
        io.quarkus.mongodb.panache.PanacheQuery<NewsDocument> mockQuery = 
            Mockito.mock(io.quarkus.mongodb.panache.PanacheQuery.class);
        
        when(newsRepository.findAll()).thenReturn(mockQuery);
        when(mockQuery.page(page, size)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(emptyDocuments);
        
        // Mock mapper behavior
        when(newsMapper.toResponseList(emptyDocuments)).thenReturn(emptyResponses);
        
        // When
        List<NewsResponse> result = newsService.getNews(page, size);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verify interactions
        verify(newsRepository).findAll();
        verify(newsMapper).toResponseList(emptyDocuments);
    }
    
    @Test
    void testGetNewsById_withInvalidObjectId_shouldReturnEmpty() {
        // Given
        String invalidId = "invalid-id-format";
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(invalidId);
        
        // Then
        assertFalse(result.isPresent());
        
        // Verify no repository interaction for invalid ID
        verify(newsRepository, never()).findByIdOptional(any());
        verify(newsMapper, never()).toResponse(any());
    }
    
    @Test
    void testGetNewsById_withNullId_shouldReturnEmpty() {
        // Given
        String nullId = null;
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(nullId);
        
        // Then
        assertFalse(result.isPresent());
        
        // Verify no repository interaction for null ID
        verify(newsRepository, never()).findByIdOptional(any());
        verify(newsMapper, never()).toResponse(any());
    }
    
    @Test
    void testGetNewsById_withEmptyId_shouldReturnEmpty() {
        // Given
        String emptyId = "";
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(emptyId);
        
        // Then
        assertFalse(result.isPresent());
        
        // Verify no repository interaction for empty ID
        verify(newsRepository, never()).findByIdOptional(any());
        verify(newsMapper, never()).toResponse(any());
    }
    
    @Test
    void testGetNewsById_withWhitespaceId_shouldReturnEmpty() {
        // Given
        String whitespaceId = "   ";
        
        // When
        Optional<NewsResponse> result = newsService.getNewsById(whitespaceId);
        
        // Then
        assertFalse(result.isPresent());
        
        // Verify no repository interaction for whitespace ID
        verify(newsRepository, never()).findByIdOptional(any());
        verify(newsMapper, never()).toResponse(any());
    }
    
    @Test
    void testGetNews_withNegativePage_shouldThrowException() {
        // Given
        int negativePage = -1;
        int size = 10;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(negativePage, size);
        });
        
        // Verify no repository or mapper interaction
        verify(newsRepository, never()).findAll();
        verify(newsMapper, never()).toResponseList(anyList());
    }
    
    @Test
    void testGetNews_withNegativeSize_shouldThrowException() {
        // Given
        int page = 0;
        int negativeSize = -1;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(page, negativeSize);
        });
        
        // Verify no repository or mapper interaction
        verify(newsRepository, never()).findAll();
        verify(newsMapper, never()).toResponseList(anyList());
    }
    
    @Test
    void testGetNews_withZeroSize_shouldThrowException() {
        // Given
        int page = 0;
        int zeroSize = 0;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(page, zeroSize);
        });
        
        // Verify no repository or mapper interaction
        verify(newsRepository, never()).findAll();
        verify(newsMapper, never()).toResponseList(anyList());
    }
    
    @Test
    void testCount_withZeroCount_shouldReturnZero() {
        // Given
        long expectedCount = 0L;
        
        // Mock repository behavior
        when(newsRepository.count()).thenReturn(expectedCount);
        
        // When
        long result = newsService.count();
        
        // Then
        assertEquals(0, result);
        
        // Verify interactions
        verify(newsRepository).count();
    }
}
