package com.gepardec.bookshop.examples.example05;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.bookshop.persistence.entity.Book;
import com.gepardec.bookshop.rest.dto.BookRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class QueriesTest {

    @Inject
    ObjectMapper mapper;

    @Test
    void getBookTitleAndPublicationYearByTitle() throws JsonProcessingException {
        createBooks();

        JsonPath jsonPath = given()
                .when().get("/history/book/title/%s".formatted("Java"))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        List<Book> books = jsonPath.getList("$", Book.class);
        assertThat(books)
                .hasSize(2)
                .allSatisfy(b -> assertThat(b.getTitle()).contains("Java"));
    }

    @Test
    void getBookTitleAndPublicationYearByTitleAndOrderByPublicationYear() throws JsonProcessingException {
        createBooks();

        JsonPath jsonPath = given()
                .when().get("/history/book/title/%s/ordered".formatted("Java"))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        int publicationYear1 = jsonPath.getInt("[0].publicationYear");
        assertThat(publicationYear1).isEqualTo(2025);

        int publicationYear2 = jsonPath.getInt("[1].publicationYear");
        assertThat(publicationYear2).isEqualTo(1975);

    }

    private void createBooks() throws JsonProcessingException {
        BookRequest book1 = new BookRequest();
        book1.setIsbn("9780747532799");
        book1.setAuthorId(1L);
        book1.setTitle("Java fundamentals");
        book1.setPublicationYear(1975);

        BookRequest book2 = new BookRequest();
        book2.setIsbn("9780747532899");
        book2.setAuthorId(1L);
        book2.setTitle("New features - Java 25");
        book2.setPublicationYear(2025);

        given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(book1))
                .when()
                .post("/book")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(book2))
                .when()
                .post("/book")
                .then()
                .statusCode(201);


    }
}
