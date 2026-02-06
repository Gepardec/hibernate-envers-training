package com.gepardec.bookshop.basic.basic05;

import com.gepardec.bookshop.rest.dto.AuthorRevisionDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class HorizontalAndVerticalQueriesTest {

    @Inject
    EntityManager em;

    @Transactional
    public void doMagicStuff() {
        // January 1, 1970
        em.createNativeQuery("UPDATE revision SET timestamp = 0 WHERE id = 1")
                .executeUpdate();
    }

    @Nested
    class HorizontalQuery {

        @Test
        void getBookByTimestamp() {
            doMagicStuff();

            JsonPath jsonPath = given()
                    .when().get("/history/book/timestamp/%s".formatted(Instant.EPOCH))
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath();

            int id = jsonPath.getInt("[0].id");
            assertThat(id).isEqualTo(1);
        }
    }

    @Nested
    class VerticalQuery {

        @Test
        public void findAuthorRevisionsByName() {
            given()
                    .when()
                    .delete("/author/1")
                    .then()
                    .statusCode(204);

            JsonPath jsonPath = given()
                    .when().get("/history/author/authorName/%s".formatted("Stephen"))
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath();

            List<AuthorRevisionDto> revisions = jsonPath.getList("$", AuthorRevisionDto.class);

            assertThat(revisions)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.comparing(AuthorRevisionDto::revisionType).reversed())
                    .allSatisfy(revision -> {
                        assertThat(revision.name())
                                .contains("Stephen");
                    });
        }
    }
}
