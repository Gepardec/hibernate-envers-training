package com.gepardec.bookshop.examples.example07;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.bookshop.persistence.entity.Author;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class SearchHistoryTest {

    @Inject
    ObjectMapper mapper;

    @Test
    void shouldGetAuthorHistoryByUsername() throws JsonProcessingException {
        String username = "twoleftfeet";

        Author author = new Author();
        author.setName("Hermann Hesse");
        author.setEmail("hermann.hesse@gepardec.com");
        author.setInternalNotes("my notes.");

        given()
                .header("username", username)
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(author))
                .when()
                .post("/author")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        String retrievedUsername = given()
                .when()
                .get("history/author/username/%s".formatted(username))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("[0].username");

        assertThat(retrievedUsername)
                .isNotNull()
                .isEqualTo(username);
    }
}
