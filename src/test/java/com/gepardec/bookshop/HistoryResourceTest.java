package com.gepardec.bookshop;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class HistoryResourceTest {

    @Nested
    class BookHistory {

        @Test
        void shouldReturnAuditEntries() {
            Long bookId = given()
                    .when().get("/book")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath().getLong("[0].id");

            given()
                    .pathParam("bookId", bookId)
                    .when().get("/history/book/{bookId}")
                    .then()
                    .statusCode(200)
                    .body("$", not(empty()));
        }
    }

    @Nested
    class AuthorHistory {

        @Test
        void shouldReturnAuditEntities() {
            Long authorId = given()
                    .when().get("/author")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath().getLong("[0].id");

            given()
                    .pathParam("authorId", authorId)
                    .when().get("/history/author/{authorId}")
                    .then()
                    .statusCode(200)
                    .body("$", not(empty()));
        }

        @Test
        void shouldReturnRevisionDtos() {
            Long authorId = given()
                    .when().get("/author")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath().getLong("[0].id");

            given()
                    .pathParam("authorId", authorId)
                    .when().get("/history/author/{authorId}/full")
                    .then()
                    .statusCode(200)
                    .body("$", not(empty()))
                    .body("[0].authorId", equalTo(authorId.intValue()))
                    .body("[0].revisionId", notNullValue())
                    .body("[0].revisionTimestamp", notNullValue());
        }
    }
}
