package org.gfoo.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NewsDocumentTest {

    @BeforeEach
    void setup() {
        // Clean up before each test
        NewsDocument.deleteAll();
    }

    @Test
    void testSaveNewsDocument() {
        // Create a new NewsDocument
        NewsDocument news = new NewsDocument();
        news.title = "Breaking News";
        news.catchLine = "This is a test news article";
        news.isEvent = false;
        news.language = "en";
        news.status = "usable";
        news.userCreated = "testUser";
        news.userLastModified = "testUser";
        news.dateCreated = new Date();
        news.dateLastModified = new Date();
        
        // Persist the entity
        news.persist();
        
        // Verify it was saved
        assertNotNull(news.id);
        assertEquals(1, NewsDocument.count());
    }

    @Test
    void testFindNewsDocument() {
        // Create and persist a news document
        NewsDocument news = new NewsDocument();
        news.title = "Find Me";
        news.catchLine = "Test catchline";
        news.isEvent = true;
        news.language = "fr";
        news.status = "usable";
        news.userCreated = "admin";
        news.userLastModified = "admin";
        news.dateCreated = new Date();
        news.dateLastModified = new Date();
        news.persist();
        
        // Find the document by ID
        NewsDocument found = NewsDocument.findById(news.id);
        
        // Verify the fields
        assertNotNull(found);
        assertEquals("Find Me", found.title);
        assertEquals("Test catchline", found.catchLine);
        assertTrue(found.isEvent);
        assertEquals("fr", found.language);
        assertEquals("usable", found.status);
        assertEquals("admin", found.userCreated);
        assertEquals("admin", found.userLastModified);
        assertNotNull(found.dateCreated);
        assertNotNull(found.dateLastModified);
    }

    @Test
    void testDeleteNewsDocument() {
        // Create and persist a news document
        NewsDocument news = new NewsDocument();
        news.title = "Delete Me";
        news.catchLine = "This will be deleted";
        news.isEvent = false;
        news.language = "en";
        news.status = "deleted";
        news.userCreated = "testUser";
        news.userLastModified = "testUser";
        news.dateCreated = new Date();
        news.dateLastModified = new Date();
        news.persist();
        
        // Verify it was persisted
        assertEquals(1, NewsDocument.count());
        
        // Delete the document
        news.delete();
        
        // Verify it was deleted
        assertEquals(0, NewsDocument.count());
        assertNull(NewsDocument.findById(news.id));
    }

    @Test
    void testUpdateNewsDocument() {
        // Create and persist a news document
        NewsDocument news = new NewsDocument();
        news.title = "Original Title";
        news.catchLine = "Original catchline";
        news.isEvent = false;
        news.language = "en";
        news.status = "usable";
        news.userCreated = "user1";
        news.userLastModified = "user1";
        news.dateCreated = new Date();
        news.dateLastModified = new Date();
        news.persist();
        
        // Update the document
        news.title = "Updated Title";
        news.userLastModified = "user2";
        news.dateLastModified = new Date();
        news.update();
        
        // Retrieve and verify the update
        NewsDocument updated = NewsDocument.findById(news.id);
        assertNotNull(updated);
        assertEquals("Updated Title", updated.title);
        assertEquals("user2", updated.userLastModified);
    }

    @Test
    void testFindAllNewsDocuments() {
        // Create multiple news documents
        NewsDocument news1 = new NewsDocument();
        news1.title = "News 1";
        news1.catchLine = "Catchline 1";
        news1.isEvent = false;
        news1.language = "en";
        news1.status = "usable";
        news1.userCreated = "user1";
        news1.userLastModified = "user1";
        news1.dateCreated = new Date();
        news1.dateLastModified = new Date();
        news1.persist();
        
        NewsDocument news2 = new NewsDocument();
        news2.title = "News 2";
        news2.catchLine = "Catchline 2";
        news2.isEvent = true;
        news2.language = "fr";
        news2.status = "usable";
        news2.userCreated = "user2";
        news2.userLastModified = "user2";
        news2.dateCreated = new Date();
        news2.dateLastModified = new Date();
        news2.persist();
        
        // Verify count
        assertEquals(2, NewsDocument.count());
        
        // Verify we can list all
        var allNews = NewsDocument.listAll();
        assertEquals(2, allNews.size());
    }

    @Test
    void testFieldMapping() {
        // Create a document with all fields set
        NewsDocument news = new NewsDocument();
        news.title = "Test Title";
        news.catchLine = "Test Catchline";
        news.isEvent = true;
        news.language = "fr";
        news.status = "usable";
        news.userCreated = "creator";
        news.userLastModified = "modifier";
        Date creationDate = new Date();
        Date modificationDate = new Date();
        news.dateCreated = creationDate;
        news.dateLastModified = modificationDate;
        news.persist();
        
        // Retrieve and verify all fields are correctly mapped
        NewsDocument retrieved = NewsDocument.findById(news.id);
        assertNotNull(retrieved);
        assertEquals("Test Title", retrieved.title);
        assertEquals("Test Catchline", retrieved.catchLine);
        assertTrue(retrieved.isEvent);
        assertEquals("fr", retrieved.language);
        assertEquals("usable", retrieved.status);
        assertEquals("creator", retrieved.userCreated);
        assertEquals("modifier", retrieved.userLastModified);
        assertEquals(creationDate, retrieved.dateCreated);
        assertEquals(modificationDate, retrieved.dateLastModified);
    }
}
