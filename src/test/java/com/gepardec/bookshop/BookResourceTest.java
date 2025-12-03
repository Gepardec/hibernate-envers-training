package com.gepardec.bookshop;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class BookResourceTest {

    @Test
    void listBooks_shouldReturn200AndNonEmptyList() {
        given()
                .when().get("/book")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void getBookById_shouldReturnSingleBook() {
        // Get an existing book id from the list endpoint
        Long bookId = given()
                .when().get("/book")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getLong("[0].id");

        given()
                .pathParam("id", bookId)
                .when().get("/book/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(bookId.intValue()))
                .body("title", notNullValue());
    }

    @Test
    void createUpdateDeleteBook_happyPath() {
        // Get an existing author id to attach the new book to
        Long authorId = given()
                .when().get("/author")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getLong("[0].id");

        // Create a new book
        Long createdBookId = given()
                .contentType("application/json")
                .body("""
                        {
                          "title": "Test Book",
                          "isbn": "TEST-123",
                          "publicationYear": 2024,
                          "authorId": %d
                        }
                        """.formatted(authorId))
                .when().post("/book")
                .then()
                .statusCode(201)
                .header("Location", containsString("/books/"))
                .body("id", notNullValue())
                .body("title", equalTo("Test Book"))
                .extract()
                .jsonPath().getLong("id");

        // Update the book title
        given()
                .contentType("application/json")
                .pathParam("id", createdBookId)
                .body("""
                        {
                          "title": "Updated Test Book"
                        }
                        """)
                .when().put("/book/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdBookId.intValue()))
                .body("title", equalTo("Updated Test Book"));

        // Delete the book
        given()
                .pathParam("id", createdBookId)
                .when().delete("/book/{id}")
                .then()
                .statusCode(204);

        // Verify it is gone
        given()
                .pathParam("id", createdBookId)
                .when().get("/book/{id}")
                .then()
                .statusCode(404);
    }
}
