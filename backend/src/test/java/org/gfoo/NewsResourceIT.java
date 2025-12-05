package org.gfoo;

import org.gfoo.entity.NewsDocument;
import org.gfoo.repository.NewsRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestProfile(MongoTestProfile.class)
public class NewsResourceIT {
    
    private static final int TOTAL_TEST_ITEMS = 18;
    private static final int TOTAL_NEWS_ITEMS = 10;
    private static final int TOTAL_EVENT_ITEMS = 8;
    private static final String INVALID_OBJECT_ID = "000000000000000000000000";
    
    @Inject
    NewsRepository newsRepository;
    
    @BeforeEach
    void setup() {
        // Clean up before each test
        newsRepository.deleteAll();
        
        // Inject realistic test data - at least 15 news items
        // Mix of isEvent: true/false
        // Mix of status: usable/deleted
        // Mix of language: fr/en
        
        // Create 10 regular news items
        for (int i = 1; i <= TOTAL_NEWS_ITEMS; i++) {
            NewsDocument news = new NewsDocument();
            news.title = "Breaking News " + i;
            news.catchLine = "Important update about topic " + i;
            news.isEvent = false;
            news.language = i % 2 == 0 ? "fr" : "en";
            news.status = i % 4 == 0 ? "deleted" : "usable";
            news.userCreated = "admin";
            news.userLastModified = "admin";
            news.dateCreated = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(i));
            news.dateLastModified = new Date();
            newsRepository.persist(news);
        }
        
        // Create 8 events
        for (int i = 1; i <= TOTAL_EVENT_ITEMS; i++) {
            NewsDocument event = new NewsDocument();
            event.title = "Event " + i;
            event.catchLine = "Join us for event " + i;
            event.isEvent = true;
            event.language = i % 3 == 0 ? "en" : "fr";
            event.status = i % 5 == 0 ? "deleted" : "usable";
            event.userCreated = "eventmanager";
            event.userLastModified = "eventmanager";
            event.dateCreated = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12 * i));
            event.dateLastModified = new Date();
            newsRepository.persist(event);
        }
    }
    
    @Test
    void testGetNews_shouldReturnPagedResponse() {
        given()
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data", hasSize(greaterThan(0)))
            .body("page", equalTo(0))
            .body("size", equalTo(10));
    }
    
    @Test
    void testGetNews_withPagination_shouldReturnCorrectPage() {
        // Test page 1 with size 5
        given()
            .queryParam("page", 1)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", equalTo(1))
            .body("size", equalTo(5))
            .body("total", equalTo(TOTAL_TEST_ITEMS))
            .body("data", hasSize(5));
    }
    
    @Test
    void testGetNewsById_whenExists_shouldReturn200() {
        // Get the ID of an existing news item
        NewsDocument existingNews = newsRepository.listAll().get(0);
        String id = existingNews.id.toString();
        
        given()
            .when().get("/news/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(id))
            .body("title", equalTo(existingNews.title))
            .body("catchLine", equalTo(existingNews.catchLine))
            .body("language", equalTo(existingNews.language))
            .body("status", equalTo(existingNews.status));
    }
    
    @Test
    void testGetNewsById_whenNotExists_shouldReturn404() {
        given()
            .when().get("/news/" + INVALID_OBJECT_ID)
            .then()
            .statusCode(404);
    }
    
    @Test
    void testGetNews_verifyPagedResponseStructure() {
        given()
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("$", hasKey("data"))
            .body("$", hasKey("page"))
            .body("$", hasKey("size"))
            .body("$", hasKey("total"))
            .body("data[0]", hasKey("id"))
            .body("data[0]", hasKey("title"))
            .body("data[0]", hasKey("type"))
            .body("data[0]", hasKey("catchLine"))
            .body("data[0]", hasKey("language"))
            .body("data[0]", hasKey("status"));
    }
    
    @Test
    void testGetNews_verifyIsEventToTypeMapping() {
        // Find an event document
        NewsDocument eventDoc = newsRepository.listAll().stream()
            .filter(doc -> doc.isEvent != null && doc.isEvent)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Expected to find at least one event document in test data"));
        
        String id = eventDoc.id.toString();
        
        // Verify that isEvent=true maps to type="event"
        given()
            .when().get("/news/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("type", equalTo("event"));
        
        // Find a news document
        NewsDocument newsDoc = newsRepository.listAll().stream()
            .filter(doc -> doc.isEvent != null && !doc.isEvent)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Expected to find at least one news document in test data"));
        
        String newsId = newsDoc.id.toString();
        
        // Verify that isEvent=false maps to type="news"
        given()
            .when().get("/news/" + newsId)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("type", equalTo("news"));
    }
    
    @Test
    void testGetNews_defaultPaginationReturns10Items() {
        // Default pagination should return 10 items even though we have TOTAL_TEST_ITEMS
        given()
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data", hasSize(10))
            .body("total", equalTo(TOTAL_TEST_ITEMS));
    }
    
    @Test
    void testGetNews_customPaginationPage0Size5() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", equalTo(0))
            .body("size", equalTo(5))
            .body("data", hasSize(5))
            .body("total", equalTo(TOTAL_TEST_ITEMS));
    }
    
    @Test
    void testGetNews_lastPage() {
        // Get the last page with remaining items
        // With TOTAL_TEST_ITEMS=18, pages 0-2 have 5 each (15 total), page 3 has 3 remaining
        given()
            .queryParam("page", 3)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", equalTo(3))
            .body("size", equalTo(5))
            .body("data", hasSize(3))
            .body("total", equalTo(TOTAL_TEST_ITEMS));
    }
    
    @Test
    void testGetNewsById_invalidIdFormat_shouldReturn404() {
        given()
            .when().get("/news/invalid-id-format")
            .then()
            .statusCode(404);
    }
    
    @Test
    void testGetNews_verifyMixedLanguages() {
        // Verify that response contains both French and English news
        given()
            .queryParam("size", 20)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data.findAll { it.language == 'fr' }.size()", greaterThan(0))
            .body("data.findAll { it.language == 'en' }.size()", greaterThan(0));
    }
    
    @Test
    void testGetNews_verifyMixedStatuses() {
        // Verify that response contains both usable and deleted news
        given()
            .queryParam("size", 20)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data.findAll { it.status == 'usable' }.size()", greaterThan(0))
            .body("data.findAll { it.status == 'deleted' }.size()", greaterThan(0));
    }
    
    @Test
    void testGetNews_verifyMixedTypes() {
        // Verify that response contains both news and events
        given()
            .queryParam("size", 20)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data.findAll { it.type == 'news' }.size()", greaterThan(0))
            .body("data.findAll { it.type == 'event' }.size()", greaterThan(0));
    }
}
