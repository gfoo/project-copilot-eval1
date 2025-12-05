package org.gfoo.mapper;

import org.gfoo.dto.NewsResponse;
import org.gfoo.entity.NewsDocument;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NewsMapperTest {

    @Inject
    NewsMapper newsMapper;

    @BeforeEach
    void setup() {
        // Clean up before each test
        NewsDocument.deleteAll();
    }

    @Test
    void testMapNewsDocumentToResponse() {
        // Create a NewsDocument
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = new ObjectId();
        newsDocument.title = "Breaking News";
        newsDocument.catchLine = "Important announcement";
        newsDocument.isEvent = false;
        newsDocument.language = "en";
        newsDocument.status = "usable";
        newsDocument.userCreated = "testUser";
        newsDocument.userLastModified = "testUser";
        newsDocument.dateCreated = new Date();
        newsDocument.dateLastModified = new Date();

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(newsDocument);

        // Verify mapping
        assertNotNull(response);
        assertEquals(newsDocument.id.toString(), response.id());
        assertEquals("Breaking News", response.title());
        assertEquals("news", response.type());
        assertEquals("Important announcement", response.catchLine());
        assertEquals("en", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testMapEventDocumentToResponse() {
        // Create an event NewsDocument
        NewsDocument eventDocument = new NewsDocument();
        eventDocument.id = new ObjectId();
        eventDocument.title = "Annual Conference";
        eventDocument.catchLine = "Join us for our yearly event";
        eventDocument.isEvent = true;
        eventDocument.language = "fr";
        eventDocument.status = "usable";
        eventDocument.userCreated = "admin";
        eventDocument.userLastModified = "admin";
        eventDocument.dateCreated = new Date();
        eventDocument.dateLastModified = new Date();

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(eventDocument);

        // Verify mapping
        assertNotNull(response);
        assertEquals(eventDocument.id.toString(), response.id());
        assertEquals("Annual Conference", response.title());
        assertEquals("event", response.type());
        assertEquals("Join us for our yearly event", response.catchLine());
        assertEquals("fr", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testMapNullDocument() {
        NewsResponse response = newsMapper.toResponse(null);
        assertNull(response);
    }

    @Test
    void testMapListOfDocuments() {
        // Create multiple NewsDocuments
        List<NewsDocument> documents = new ArrayList<>();

        NewsDocument news1 = new NewsDocument();
        news1.id = new ObjectId();
        news1.title = "News 1";
        news1.catchLine = "First news";
        news1.isEvent = false;
        news1.language = "en";
        news1.status = "usable";
        documents.add(news1);

        NewsDocument news2 = new NewsDocument();
        news2.id = new ObjectId();
        news2.title = "Event 1";
        news2.catchLine = "First event";
        news2.isEvent = true;
        news2.language = "fr";
        news2.status = "usable";
        documents.add(news2);

        // Map to list of NewsResponse
        List<NewsResponse> responses = newsMapper.toResponseList(documents);

        // Verify mapping
        assertNotNull(responses);
        assertEquals(2, responses.size());

        NewsResponse response1 = responses.get(0);
        assertEquals(news1.id.toString(), response1.id());
        assertEquals("News 1", response1.title());
        assertEquals("news", response1.type());

        NewsResponse response2 = responses.get(1);
        assertEquals(news2.id.toString(), response2.id());
        assertEquals("Event 1", response2.title());
        assertEquals("event", response2.type());
    }

    @Test
    void testMapEmptyList() {
        List<NewsDocument> documents = new ArrayList<>();
        List<NewsResponse> responses = newsMapper.toResponseList(documents);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testMapNullList() {
        List<NewsResponse> responses = newsMapper.toResponseList(null);
        assertNull(responses);
    }

    @Test
    void testMapWithPersistedDocument() {
        // Create and persist a NewsDocument
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.title = "Persisted News";
        newsDocument.catchLine = "This is persisted in MongoDB";
        newsDocument.isEvent = false;
        newsDocument.language = "en";
        newsDocument.status = "usable";
        newsDocument.userCreated = "testUser";
        newsDocument.userLastModified = "testUser";
        newsDocument.dateCreated = new Date();
        newsDocument.dateLastModified = new Date();
        newsDocument.persist();

        // Verify it was persisted
        assertNotNull(newsDocument.id);

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(newsDocument);

        // Verify mapping
        assertNotNull(response);
        assertEquals(newsDocument.id.toString(), response.id());
        assertEquals("Persisted News", response.title());
        assertEquals("news", response.type());
        assertEquals("This is persisted in MongoDB", response.catchLine());
        assertEquals("en", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testMapWithRetrievedDocument() {
        // Create and persist a NewsDocument
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.title = "Retrieved News";
        newsDocument.catchLine = "This is retrieved from MongoDB";
        newsDocument.isEvent = true;
        newsDocument.language = "fr";
        newsDocument.status = "usable";
        newsDocument.userCreated = "testUser";
        newsDocument.userLastModified = "testUser";
        newsDocument.dateCreated = new Date();
        newsDocument.dateLastModified = new Date();
        newsDocument.persist();

        // Retrieve the document
        NewsDocument retrieved = NewsDocument.findById(newsDocument.id);
        assertNotNull(retrieved);

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(retrieved);

        // Verify mapping
        assertNotNull(response);
        assertEquals(retrieved.id.toString(), response.id());
        assertEquals("Retrieved News", response.title());
        assertEquals("event", response.type());
        assertEquals("This is retrieved from MongoDB", response.catchLine());
        assertEquals("fr", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testMapDeletedStatusDocument() {
        // Create a NewsDocument with deleted status
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = new ObjectId();
        newsDocument.title = "Deleted News";
        newsDocument.catchLine = "This news is marked as deleted";
        newsDocument.isEvent = false;
        newsDocument.language = "en";
        newsDocument.status = "deleted";
        newsDocument.userCreated = "testUser";
        newsDocument.userLastModified = "testUser";
        newsDocument.dateCreated = new Date();
        newsDocument.dateLastModified = new Date();

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(newsDocument);

        // Verify mapping
        assertNotNull(response);
        assertEquals("deleted", response.status());
    }

    @Test
    void testObjectIdToStringConversion() {
        // Create a specific ObjectId
        ObjectId objectId = new ObjectId("507f1f77bcf86cd799439011");

        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = objectId;
        newsDocument.title = "Test ObjectId";
        newsDocument.catchLine = "Testing ObjectId conversion";
        newsDocument.isEvent = false;
        newsDocument.language = "en";
        newsDocument.status = "usable";

        // Map to NewsResponse
        NewsResponse response = newsMapper.toResponse(newsDocument);

        // Verify ObjectId is correctly converted to String
        assertNotNull(response);
        assertEquals("507f1f77bcf86cd799439011", response.id());
        assertTrue(response.id() instanceof String);
    }

    @Test
    void testIsEventTrueToTypeEvent() {
        NewsDocument eventDocument = new NewsDocument();
        eventDocument.id = new ObjectId();
        eventDocument.title = "Event";
        eventDocument.isEvent = true;

        NewsResponse response = newsMapper.toResponse(eventDocument);

        assertEquals("event", response.type());
    }

    @Test
    void testIsEventFalseToTypeNews() {
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = new ObjectId();
        newsDocument.title = "News";
        newsDocument.isEvent = false;

        NewsResponse response = newsMapper.toResponse(newsDocument);

        assertEquals("news", response.type());
    }

    @Test
    void testNullObjectId() {
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = null;
        newsDocument.title = "News without ID";
        newsDocument.isEvent = false;

        NewsResponse response = newsMapper.toResponse(newsDocument);

        assertNotNull(response);
        assertNull(response.id());
    }

    @Test
    void testNullIsEvent() {
        NewsDocument newsDocument = new NewsDocument();
        newsDocument.id = new ObjectId();
        newsDocument.title = "News without isEvent";
        newsDocument.isEvent = null;

        NewsResponse response = newsMapper.toResponse(newsDocument);

        assertNotNull(response);
        assertNull(response.type());
    }
}
