package com.gepardec.bookshop.rest.dto;

import org.hibernate.envers.RevisionType;

public record AuthorRevisionDto(
        Long authorId,
        String name,
        int revisionId,
        long revisionTimestamp,
        String username,
        RevisionType revisionType
) {
}