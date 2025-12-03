package com.example.dto;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NewsResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRecordCreation() {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Breaking News",
            "news",
            "Important announcement",
            "en",
            "usable"
        );

        assertNotNull(response);
        assertEquals("507f1f77bcf86cd799439011", response.id());
        assertEquals("Breaking News", response.title());
        assertEquals("news", response.type());
        assertEquals("Important announcement", response.catchLine());
        assertEquals("en", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testRecordWithEventType() {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439012",
            "Company Event",
            "event",
            "Join us for the annual conference",
            "fr",
            "usable"
        );

        assertEquals("event", response.type());
        assertEquals("fr", response.language());
    }

    @Test
    void testJsonSerialization() throws Exception {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Test Title",
            "news",
            "Test Catchline",
            "en",
            "usable"
        );

        String json = objectMapper.writeValueAsString(response);

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"507f1f77bcf86cd799439011\""));
        assertTrue(json.contains("\"title\":\"Test Title\""));
        assertTrue(json.contains("\"type\":\"news\""));
        assertTrue(json.contains("\"catchLine\":\"Test Catchline\""));
        assertTrue(json.contains("\"language\":\"en\""));
        assertTrue(json.contains("\"status\":\"usable\""));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = """
            {
                "id": "507f1f77bcf86cd799439011",
                "title": "Test Title",
                "type": "news",
                "catchLine": "Test Catchline",
                "language": "en",
                "status": "usable"
            }
            """;

        NewsResponse response = objectMapper.readValue(json, NewsResponse.class);

        assertNotNull(response);
        assertEquals("507f1f77bcf86cd799439011", response.id());
        assertEquals("Test Title", response.title());
        assertEquals("news", response.type());
        assertEquals("Test Catchline", response.catchLine());
        assertEquals("en", response.language());
        assertEquals("usable", response.status());
    }

    @Test
    void testNullValues() {
        NewsResponse response = new NewsResponse(
            null,
            null,
            null,
            null,
            null,
            null
        );

        assertNotNull(response);
        assertNull(response.id());
        assertNull(response.title());
        assertNull(response.type());
        assertNull(response.catchLine());
        assertNull(response.language());
        assertNull(response.status());
    }

    @Test
    void testEmptyValues() {
        NewsResponse response = new NewsResponse(
            "",
            "",
            "",
            "",
            "",
            ""
        );

        assertNotNull(response);
        assertEquals("", response.id());
        assertEquals("", response.title());
        assertEquals("", response.type());
        assertEquals("", response.catchLine());
        assertEquals("", response.language());
        assertEquals("", response.status());
    }

    @Test
    void testJsonSerializationWithNullValues() throws Exception {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439011",
            null,
            "news",
            null,
            "en",
            "usable"
        );

        String json = objectMapper.writeValueAsString(response);

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"507f1f77bcf86cd799439011\""));
        assertTrue(json.contains("\"title\":null"));
        assertTrue(json.contains("\"catchLine\":null"));
    }

    @Test
    void testRecordEquality() {
        NewsResponse response1 = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Title",
            "news",
            "Catchline",
            "en",
            "usable"
        );

        NewsResponse response2 = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Title",
            "news",
            "Catchline",
            "en",
            "usable"
        );

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testRecordToString() {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Title",
            "news",
            "Catchline",
            "en",
            "usable"
        );

        String toString = response.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("507f1f77bcf86cd799439011"));
        assertTrue(toString.contains("Title"));
        assertTrue(toString.contains("news"));
    }

    @Test
    void testDeletedStatus() {
        NewsResponse response = new NewsResponse(
            "507f1f77bcf86cd799439011",
            "Deleted News",
            "news",
            "This was deleted",
            "en",
            "deleted"
        );

        assertEquals("deleted", response.status());
    }
}
