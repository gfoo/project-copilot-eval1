package com.example;

import com.example.entity.NewsDocument;
import com.example.repository.NewsRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class NewsResourceTest {

    @Inject
    NewsRepository newsRepository;

    @BeforeEach
    void setup() {
        // Clean up before each test
        newsRepository.deleteAll();
        
        // Inject test data
        for (int i = 1; i <= 15; i++) {
            NewsDocument news = new NewsDocument();
            news.title = "News " + i;
            news.catchLine = "Description " + i;
            news.isEvent = i % 3 == 0; // Every third is an event
            news.language = i % 2 == 0 ? "fr" : "en";
            news.status = "usable";
            news.userCreated = "testuser";
            news.userLastModified = "testuser";
            news.dateCreated = new Date();
            news.dateLastModified = new Date();
            newsRepository.persist(news);
        }
    }

    @Test
    void testGetNews_DefaultPagination() {
        given()
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(0))
            .body("size", is(10))
            .body("total", is(15))
            .body("data", hasSize(10));
    }

    @Test
    void testGetNews_CustomPagination() {
        given()
            .queryParam("page", 1)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(1))
            .body("size", is(5))
            .body("total", is(15))
            .body("data", hasSize(5));
    }

    @Test
    void testGetNews_VerifyPagedResponseStructure() {
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
    void testGetNews_LastPage() {
        given()
            .queryParam("page", 2)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(2))
            .body("size", is(5))
            .body("total", is(15))
            .body("data", hasSize(5));
    }

    @Test
    void testGetNews_EmptyPage() {
        given()
            .queryParam("page", 10)
            .queryParam("size", 10)
            .when().get("/news")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(10))
            .body("size", is(10))
            .body("total", is(15))
            .body("data", hasSize(0));
    }

    @Test
    void testGetNewsById_ExistingNews() {
        // Get the ID of an existing news item
        NewsDocument existingNews = newsRepository.listAll().get(0);
        String id = existingNews.id.toString();
        
        given()
            .when().get("/news/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", is(id))
            .body("title", is(existingNews.title))
            .body("catchLine", is(existingNews.catchLine))
            .body("language", is(existingNews.language))
            .body("status", is(existingNews.status));
    }

    @Test
    void testGetNewsById_NonExistingNews() {
        // Use a valid ObjectId format but non-existing ID
        String nonExistingId = new ObjectId().toString();
        
        given()
            .when().get("/news/" + nonExistingId)
            .then()
            .statusCode(404);
    }

    @Test
    void testGetNewsById_InvalidIdFormat() {
        given()
            .when().get("/news/invalid-id-format")
            .then()
            .statusCode(404);
    }

    @Test
    void testGetNewsById_VerifyTypeMapping() {
        // Find an event document
        NewsDocument eventDoc = newsRepository.listAll().stream()
            .filter(doc -> doc.isEvent != null && doc.isEvent)
            .findFirst()
            .orElseThrow();
        
        String id = eventDoc.id.toString();
        
        given()
            .when().get("/news/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("type", is("event"));
    }

    @Test
    void testGetNews_VerifyPaginationConsistency() {
        // Get all items at once
        int totalItems = given()
            .queryParam("size", 100)
            .when().get("/news")
            .then()
            .statusCode(200)
            .extract()
            .path("data.size()");

        // Get items across multiple pages
        int page1Size = given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .extract()
            .path("data.size()");

        int page2Size = given()
            .queryParam("page", 1)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .extract()
            .path("data.size()");

        int page3Size = given()
            .queryParam("page", 2)
            .queryParam("size", 5)
            .when().get("/news")
            .then()
            .statusCode(200)
            .extract()
            .path("data.size()");

        // Verify consistency
        assertEquals(totalItems, page1Size + page2Size + page3Size);
    }

    @Test
    void testGetNews_NegativePage() {
        // Test with negative page number - should return 500 due to IllegalArgumentException
        given()
            .queryParam("page", -1)
            .queryParam("size", 10)
            .when().get("/news")
            .then()
            .statusCode(500);
    }

    @Test
    void testGetNews_NegativeSize() {
        // Test with negative size - should return 500 due to IllegalArgumentException
        given()
            .queryParam("page", 0)
            .queryParam("size", -1)
            .when().get("/news")
            .then()
            .statusCode(500);
    }

    @Test
    void testGetNews_ZeroSize() {
        // Test with zero size - should return 500 due to IllegalArgumentException
        given()
            .queryParam("page", 0)
            .queryParam("size", 0)
            .when().get("/news")
            .then()
            .statusCode(500);
    }
}
