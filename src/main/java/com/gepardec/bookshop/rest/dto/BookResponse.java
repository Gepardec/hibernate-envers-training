package com.gepardec.bookshop.rest.dto;

import com.gepardec.bookshop.persistence.entity.Book;

public class BookResponse {

    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Long authorId;
    private String authorName;

    public static BookResponse fromEntity(Book book) {
        BookResponse dto = new BookResponse();
        dto.id = book.getId();
        dto.title = book.getTitle();
        dto.isbn = book.getIsbn();
        dto.publicationYear = book.getPublicationYear();
        dto.authorId = book.getAuthor().getId();
        dto.authorName = book.getAuthor().getName();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }
}
