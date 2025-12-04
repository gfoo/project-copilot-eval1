package com.example.repository;

import com.example.entity.NewsDocument;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NewsRepositoryTest {

    @Inject
    NewsRepository newsRepository;

    @BeforeEach
    void setup() {
        // Clean up before each test
        newsRepository.deleteAll();
        
        // Inject test data
        NewsDocument news1 = new NewsDocument();
        news1.title = "English News 1";
        news1.catchLine = "First English news";
        news1.isEvent = false;
        news1.language = "en";
        news1.status = "usable";
        news1.userCreated = "user1";
        news1.userLastModified = "user1";
        news1.dateCreated = new Date();
        news1.dateLastModified = new Date();
        newsRepository.persist(news1);

        NewsDocument news2 = new NewsDocument();
        news2.title = "French News 1";
        news2.catchLine = "First French news";
        news2.isEvent = true;
        news2.language = "fr";
        news2.status = "draft";
        news2.userCreated = "user2";
        news2.userLastModified = "user2";
        news2.dateCreated = new Date();
        news2.dateLastModified = new Date();
        newsRepository.persist(news2);

        NewsDocument news3 = new NewsDocument();
        news3.title = "English News 2";
        news3.catchLine = "Second English news";
        news3.isEvent = false;
        news3.language = "en";
        news3.status = "usable";
        news3.userCreated = "user3";
        news3.userLastModified = "user3";
        news3.dateCreated = new Date();
        news3.dateLastModified = new Date();
        newsRepository.persist(news3);

        NewsDocument news4 = new NewsDocument();
        news4.title = "French News 2";
        news4.catchLine = "Second French news";
        news4.isEvent = false;
        news4.language = "fr";
        news4.status = "usable";
        news4.userCreated = "user4";
        news4.userLastModified = "user4";
        news4.dateCreated = new Date();
        news4.dateLastModified = new Date();
        newsRepository.persist(news4);

        NewsDocument news5 = new NewsDocument();
        news5.title = "German News 1";
        news5.catchLine = "First German news";
        news5.isEvent = true;
        news5.language = "de";
        news5.status = "archived";
        news5.userCreated = "user5";
        news5.userLastModified = "user5";
        news5.dateCreated = new Date();
        news5.dateLastModified = new Date();
        newsRepository.persist(news5);
    }

    @Test
    void testFindAll() {
        // Test findAll method (inherited from Panache)
        List<NewsDocument> allNews = newsRepository.listAll();
        
        assertNotNull(allNews);
        assertEquals(5, allNews.size());
    }

    @Test
    void testFindById() {
        // Get the first news document
        List<NewsDocument> allNews = newsRepository.listAll();
        assertFalse(allNews.isEmpty());
        
        NewsDocument firstNews = allNews.get(0);
        
        // Test findById method
        NewsDocument found = newsRepository.findById(firstNews.id);
        
        assertNotNull(found);
        assertEquals(firstNews.id, found.id);
        assertEquals(firstNews.title, found.title);
    }

    @Test
    void testFindByLanguage() {
        // Test findByLanguage for English
        List<NewsDocument> englishNews = newsRepository.findByLanguage("en");
        
        assertNotNull(englishNews);
        assertEquals(2, englishNews.size());
        assertTrue(englishNews.stream().allMatch(news -> "en".equals(news.language)));
        
        // Test findByLanguage for French
        List<NewsDocument> frenchNews = newsRepository.findByLanguage("fr");
        
        assertNotNull(frenchNews);
        assertEquals(2, frenchNews.size());
        assertTrue(frenchNews.stream().allMatch(news -> "fr".equals(news.language)));
        
        // Test findByLanguage for German
        List<NewsDocument> germanNews = newsRepository.findByLanguage("de");
        
        assertNotNull(germanNews);
        assertEquals(1, germanNews.size());
        assertTrue(germanNews.stream().allMatch(news -> "de".equals(news.language)));
        
        // Test findByLanguage for non-existent language
        List<NewsDocument> spanishNews = newsRepository.findByLanguage("es");
        
        assertNotNull(spanishNews);
        assertTrue(spanishNews.isEmpty());
    }

    @Test
    void testFindByStatus() {
        // Test findByStatus for "usable"
        List<NewsDocument> usableNews = newsRepository.findByStatus("usable");
        
        assertNotNull(usableNews);
        assertEquals(3, usableNews.size());
        assertTrue(usableNews.stream().allMatch(news -> "usable".equals(news.status)));
        
        // Test findByStatus for "draft"
        List<NewsDocument> draftNews = newsRepository.findByStatus("draft");
        
        assertNotNull(draftNews);
        assertEquals(1, draftNews.size());
        assertTrue(draftNews.stream().allMatch(news -> "draft".equals(news.status)));
        
        // Test findByStatus for "archived"
        List<NewsDocument> archivedNews = newsRepository.findByStatus("archived");
        
        assertNotNull(archivedNews);
        assertEquals(1, archivedNews.size());
        assertTrue(archivedNews.stream().allMatch(news -> "archived".equals(news.status)));
        
        // Test findByStatus for non-existent status
        List<NewsDocument> publishedNews = newsRepository.findByStatus("published");
        
        assertNotNull(publishedNews);
        assertTrue(publishedNews.isEmpty());
    }

    @Test
    void testFindUsableNews() {
        // Test findUsableNews method
        List<NewsDocument> usableNews = newsRepository.findUsableNews();
        
        assertNotNull(usableNews);
        assertEquals(3, usableNews.size());
        assertTrue(usableNews.stream().allMatch(news -> "usable".equals(news.status)));
    }

    @Test
    void testPagination() {
        // Test Panache pagination
        PanacheQuery<NewsDocument> query = newsRepository.findAll();
        
        // Test first page with 2 items per page
        List<NewsDocument> firstPage = query.page(Page.ofSize(2)).list();
        assertEquals(2, firstPage.size());
        
        // Test second page
        List<NewsDocument> secondPage = query.page(Page.of(1, 2)).list();
        assertEquals(2, secondPage.size());
        
        // Verify that pages contain different items
        assertNotEquals(firstPage.get(0).id, secondPage.get(0).id);
        
        // Test total count
        long totalCount = query.count();
        assertEquals(5, totalCount);
        
        // Test last page
        List<NewsDocument> lastPage = query.page(Page.of(2, 2)).list();
        assertEquals(1, lastPage.size());
    }
}
