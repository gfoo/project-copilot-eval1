package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelloServiceTest {

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService();
    }

    @Test
    void testGetGreetingWithoutName() {
        String greeting = helloService.getGreeting(null);

        assertNotNull(greeting);
        assertEquals("Hello from Quarkus REST", greeting);
    }

    @Test
    void testGetGreetingWithName() {
        String greeting = helloService.getGreeting("John");

        assertNotNull(greeting);
        assertEquals("Hello John from Quarkus REST", greeting);
    }
}
