package com.gepardec.bookshop.basic.basic01;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class AuditedTest {

    @Test
    void authorShouldBeAudited() {
        given()
                .when().get("/history/author/1")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void bookShouldBeAudited() {
        given()
                .when().get("/history/book/1")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }
}
