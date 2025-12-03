package com.gepardec.bookshop.basic.basic03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.repository.AuthorRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class UsernameRevisionTest {

    @Inject
    ObjectMapper mapper;

    @Test
    void shouldSetUsername() throws JsonProcessingException {
        String username = "twoleftfeet";

        Author author = new Author();
        author.setName("Hermann Hesse");
        author.setEmail("hermann.hesse@gepardec.com");
        author.setInternalNotes("my notes.");

        long id = given()
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

        JsonPath jsonPath = given()
                .when().get("/history/author/%s/full".formatted(id))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String result = jsonPath.getString("[0].username");
        assertThat(result)
                .isNotNull()
                .isEqualTo(username);
    }
}
