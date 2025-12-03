package com.gepardec.bookshop.basic.basic04;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SearchHistoryTest {

    @Inject
    ObjectMapper mapper;

    @Test
    void shouldGetAuthorHistoryByUsername() {
        // TODO
    }
}
