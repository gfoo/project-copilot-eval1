package org.gfoo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MongoDBConnectionTest {

    @BeforeEach
    void setup() {
        // Clean up before each test
        TestEntity.deleteAll();
    }

    @Test
    void testMongoDBConnection() {
        // Test that we can persist and retrieve an entity
        TestEntity entity = new TestEntity();
        entity.name = "Test Entity";
        entity.persist();

        // Verify entity was persisted
        TestEntity found = TestEntity.findById(entity.id);
        assertNotNull(found);
        assertEquals("Test Entity", found.name);
    }

    @Test
    void testMongoDBCount() {
        // Test basic count operation
        long count = TestEntity.count();
        assertEquals(0, count);
        
        // Add an entity
        TestEntity entity = new TestEntity();
        entity.name = "Test Count";
        entity.persist();
        
        // Verify count increased
        assertEquals(1, TestEntity.count());
    }
}
