package com.gepardec.bookshop;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthorResourceTest {

    @Test
    void listAuthors_shouldReturn200AndNonEmptyList() {
        given()
                .when().get("/author")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void getAuthorById_shouldReturnSingleAuthor() {
        Long authorId = given()
                .when().get("/author")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getLong("[0].id");

        given()
                .pathParam("id", authorId)
                .when().get("/author/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(authorId.intValue()))
                .body("name", notNullValue());
    }

    @Test
    void createAuthor_shouldReturn201AndCreatedAuthor() {
        Long createdAuthorId = given()
                .contentType("application/json")
                .body("""
                        {
                          "name": "Test Author",
                          "email": "test.author@example.com"
                        }
                        """)
                .when().post("/author")
                .then()
                .statusCode(201)
                .header("Location", containsString("/authors/"))
                .body("id", notNullValue())
                .body("name", equalTo("Test Author"))
                .extract()
                .jsonPath().getLong("id");

        // basic sanity check that we can read it back
        given()
                .pathParam("id", createdAuthorId)
                .when().get("/author/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdAuthorId.intValue()))
                .body("name", equalTo("Test Author"));
    }
}
