package org.gfoo;

import org.gfoo.entity.NewsDocument;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MongoDBConnectionTest {

    @BeforeEach
    void setup() {
        // Clean up before each test
        NewsDocument.deleteAll();
    }

    @Test
    void testMongoDBConnection() {
        // Test that we can persist and retrieve an entity
        NewsDocument entity = new NewsDocument();
        entity.title = "Test Entity";
        entity.persist();

        // Verify entity was persisted
        NewsDocument found = NewsDocument.findById(entity.id);
        assertNotNull(found);
        assertEquals("Test Entity", found.title);
    }

    @Test
    void testMongoDBCount() {
        // Test basic count operation
        long count = NewsDocument.count();
        assertEquals(0, count);
        
        // Add an entity
        NewsDocument entity = new NewsDocument();
        entity.title = "Test Count";
        entity.persist();
        
        // Verify count increased
        assertEquals(1, NewsDocument.count());
    }
}
