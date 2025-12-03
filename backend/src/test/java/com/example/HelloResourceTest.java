package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class HelloResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testHelloEndpointWithName() {
        given()
                .queryParam("name", "John")
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello John from Quarkus REST"));
    }
}
