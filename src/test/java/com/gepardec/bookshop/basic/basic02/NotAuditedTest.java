package com.gepardec.bookshop.basic.basic02;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class NotAuditedTest {

    @Test
    void internalNotesShouldNotBeAudited() {
        JsonPath jsonPath = given()
                .when().get("/history/author/1")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String internalNotes = jsonPath.getString("[0].internalNotes");
        assertThat(internalNotes).isNull();

        String authorName = jsonPath.getString("[0].name");
        assertThat(authorName).isNotNull();
    }
}
