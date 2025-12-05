package org.gfoo.service;

import org.gfoo.dto.NewsResponse;
import org.gfoo.entity.NewsDocument;
import org.gfoo.repository.NewsRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NewsServiceTest {

    @Inject
    NewsService newsService;

    @Inject
    NewsRepository newsRepository;

    @BeforeEach
    void setup() {
        // Clean up before each test
        newsRepository.deleteAll();
        
        // Inject test data
        NewsDocument news1 = new NewsDocument();
        news1.title = "Breaking News 1";
        news1.catchLine = "Important announcement";
        news1.isEvent = false;
        news1.language = "en";
        news1.status = "usable";
        news1.userCreated = "user1";
        news1.userLastModified = "user1";
        news1.dateCreated = new Date();
        news1.dateLastModified = new Date();
        newsRepository.persist(news1);

        NewsDocument news2 = new NewsDocument();
        news2.title = "Event 1";
        news2.catchLine = "Annual conference";
        news2.isEvent = true;
        news2.language = "fr";
        news2.status = "usable";
        news2.userCreated = "user2";
        news2.userLastModified = "user2";
        news2.dateCreated = new Date();
        news2.dateLastModified = new Date();
        newsRepository.persist(news2);

        NewsDocument news3 = new NewsDocument();
        news3.title = "Breaking News 2";
        news3.catchLine = "Second important news";
        news3.isEvent = false;
        news3.language = "en";
        news3.status = "usable";
        news3.userCreated = "user3";
        news3.userLastModified = "user3";
        news3.dateCreated = new Date();
        news3.dateLastModified = new Date();
        newsRepository.persist(news3);

        NewsDocument news4 = new NewsDocument();
        news4.title = "Breaking News 3";
        news4.catchLine = "Third important news";
        news4.isEvent = false;
        news4.language = "en";
        news4.status = "draft";
        news4.userCreated = "user4";
        news4.userLastModified = "user4";
        news4.dateCreated = new Date();
        news4.dateLastModified = new Date();
        newsRepository.persist(news4);

        NewsDocument news5 = new NewsDocument();
        news5.title = "Event 2";
        news5.catchLine = "Workshop session";
        news5.isEvent = true;
        news5.language = "de";
        news5.status = "usable";
        news5.userCreated = "user5";
        news5.userLastModified = "user5";
        news5.dateCreated = new Date();
        news5.dateLastModified = new Date();
        newsRepository.persist(news5);
    }

    @Test
    void testGetNewsWithPagination() {
        // Test first page with 2 items
        List<NewsResponse> firstPage = newsService.getNews(0, 2);
        
        assertNotNull(firstPage);
        assertEquals(2, firstPage.size());
        
        // Verify responses are properly mapped
        firstPage.forEach(response -> {
            assertNotNull(response.id());
            assertNotNull(response.title());
            assertNotNull(response.type());
        });
    }

    @Test
    void testGetNewsWithDifferentPageSizes() {
        // Test with page size 3
        List<NewsResponse> page = newsService.getNews(0, 3);
        
        assertNotNull(page);
        assertEquals(3, page.size());
        
        // Test with page size 1
        List<NewsResponse> singlePage = newsService.getNews(0, 1);
        
        assertNotNull(singlePage);
        assertEquals(1, singlePage.size());
    }

    @Test
    void testGetNewsSecondPage() {
        // Test second page with 2 items per page
        List<NewsResponse> firstPage = newsService.getNews(0, 2);
        List<NewsResponse> secondPage = newsService.getNews(1, 2);
        
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals(2, firstPage.size());
        assertEquals(2, secondPage.size());
        
        // Verify pages contain different items
        assertNotEquals(firstPage.get(0).id(), secondPage.get(0).id());
    }

    @Test
    void testGetNewsEmptyPage() {
        // Test a page beyond available data
        List<NewsResponse> emptyPage = newsService.getNews(10, 2);
        
        assertNotNull(emptyPage);
        assertTrue(emptyPage.isEmpty());
    }

    @Test
    void testGetNewsByIdWithExistingId() {
        // Get an existing news document
        List<NewsDocument> allNews = newsRepository.listAll();
        assertFalse(allNews.isEmpty());
        
        NewsDocument firstNews = allNews.get(0);
        String id = firstNews.id.toString();
        
        // Test getNewsById with existing ID
        Optional<NewsResponse> result = newsService.getNewsById(id);
        
        assertTrue(result.isPresent());
        NewsResponse response = result.get();
        
        assertEquals(id, response.id());
        assertEquals(firstNews.title, response.title());
        assertEquals(firstNews.catchLine, response.catchLine());
        assertEquals(firstNews.language, response.language());
        assertEquals(firstNews.status, response.status());
        
        // Verify type mapping
        if (firstNews.isEvent != null) {
            String expectedType = firstNews.isEvent ? "event" : "news";
            assertEquals(expectedType, response.type());
        }
    }

    @Test
    void testGetNewsByIdWithNonExistingId() {
        // Create a valid ObjectId that doesn't exist in database
        String nonExistingId = new ObjectId().toString();
        
        // Test getNewsById with non-existing ID
        Optional<NewsResponse> result = newsService.getNewsById(nonExistingId);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetNewsByIdVerifyNewsTypeMapping() {
        // Find a news (not event) document
        List<NewsDocument> allNews = newsRepository.listAll();
        NewsDocument newsDoc = allNews.stream()
            .filter(doc -> doc.isEvent != null && !doc.isEvent)
            .findFirst()
            .orElseThrow();
        
        String id = newsDoc.id.toString();
        
        Optional<NewsResponse> result = newsService.getNewsById(id);
        
        assertTrue(result.isPresent());
        assertEquals("news", result.get().type());
    }

    @Test
    void testGetNewsByIdVerifyEventTypeMapping() {
        // Find an event document
        List<NewsDocument> allNews = newsRepository.listAll();
        NewsDocument eventDoc = allNews.stream()
            .filter(doc -> doc.isEvent != null && doc.isEvent)
            .findFirst()
            .orElseThrow();
        
        String id = eventDoc.id.toString();
        
        Optional<NewsResponse> result = newsService.getNewsById(id);
        
        assertTrue(result.isPresent());
        assertEquals("event", result.get().type());
    }

    @Test
    void testCount() {
        // Test count method
        long count = newsService.count();
        
        assertEquals(5, count);
    }

    @Test
    void testCountAfterAddingData() {
        // Verify initial count
        long initialCount = newsService.count();
        assertEquals(5, initialCount);
        
        // Add a new document
        NewsDocument newNews = new NewsDocument();
        newNews.title = "New News";
        newNews.catchLine = "Just added";
        newNews.isEvent = false;
        newNews.language = "en";
        newNews.status = "usable";
        newNews.userCreated = "testUser";
        newNews.userLastModified = "testUser";
        newNews.dateCreated = new Date();
        newNews.dateLastModified = new Date();
        newsRepository.persist(newNews);
        
        // Verify count increased
        long newCount = newsService.count();
        assertEquals(6, newCount);
    }

    @Test
    void testEntityToDtoMapping() {
        // Get all news and verify mapping for all fields
        List<NewsResponse> allNewsResponses = newsService.getNews(0, 100);
        
        assertNotNull(allNewsResponses);
        assertEquals(5, allNewsResponses.size());
        
        // Verify each response has all required fields
        allNewsResponses.forEach(response -> {
            assertNotNull(response.id(), "ID should not be null");
            assertNotNull(response.title(), "Title should not be null");
            assertNotNull(response.type(), "Type should not be null");
            assertTrue(response.type().equals("news") || response.type().equals("event"), 
                "Type should be either 'news' or 'event'");
        });
    }

    @Test
    void testGetNewsWithMinimumPageSize() {
        // Test with minimum page size 1 (edge case)
        List<NewsResponse> singleItemPage = newsService.getNews(0, 1);
        
        assertNotNull(singleItemPage);
        assertEquals(1, singleItemPage.size());
    }

    @Test
    void testGetNewsWithLargePageSize() {
        // Test with large page size that exceeds available data
        List<NewsResponse> allNews = newsService.getNews(0, 1000);
        
        assertNotNull(allNews);
        assertEquals(5, allNews.size());
    }

    @Test
    void testGetNewsPaginationConsistency() {
        // Get all data in one page
        List<NewsResponse> allAtOnce = newsService.getNews(0, 100);
        
        // Get same data in multiple pages
        List<NewsResponse> page1 = newsService.getNews(0, 2);
        List<NewsResponse> page2 = newsService.getNews(1, 2);
        List<NewsResponse> page3 = newsService.getNews(2, 2);
        
        // Verify total items match
        int paginatedTotal = page1.size() + page2.size() + page3.size();
        assertEquals(allAtOnce.size(), paginatedTotal);
    }

    @Test
    void testGetNewsWithNegativePage() {
        // Test with negative page number
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(-1, 10);
        });
    }

    @Test
    void testGetNewsWithNegativeSize() {
        // Test with negative page size
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(0, -1);
        });
    }

    @Test
    void testGetNewsWithZeroSize() {
        // Test with zero page size
        assertThrows(IllegalArgumentException.class, () -> {
            newsService.getNews(0, 0);
        });
    }

    @Test
    void testGetNewsByIdWithInvalidObjectId() {
        // Test with invalid ObjectId format
        Optional<NewsResponse> result = newsService.getNewsById("invalid-id-format");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetNewsByIdWithNull() {
        // Test with null ID
        Optional<NewsResponse> result = newsService.getNewsById(null);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetNewsByIdWithEmptyString() {
        // Test with empty string ID
        Optional<NewsResponse> result = newsService.getNewsById("");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetNewsByIdWithWhitespaceString() {
        // Test with whitespace-only ID
        Optional<NewsResponse> result = newsService.getNewsById("   ");
        
        assertFalse(result.isPresent());
    }
}
