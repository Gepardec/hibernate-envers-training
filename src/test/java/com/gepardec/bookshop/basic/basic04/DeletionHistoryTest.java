package com.gepardec.bookshop.basic.basic04;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class DeletionHistoryTest {

    @Test
    void deleteEntryShouldHavePublicationYear() {
        given()
                .when()
                .delete("/book/2")
                .then()
                .statusCode(204);

        JsonPath jsonPath = given()
                .when().get("/history/book/bookId/%s?revtype=DEL".formatted(2))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String result = jsonPath.getString("[0].publicationYear");
        assertThat(result)
                .isNotNull();
    }
}
