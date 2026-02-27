package com.gepardec.bookshop.rest.resource.history;

import com.gepardec.bookshop.persistence.entity.Book;
import com.gepardec.bookshop.persistence.repository.history.BookHistoryRepository;
import com.gepardec.bookshop.rest.dto.BookResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.envers.RevisionType;

import java.time.Instant;
import java.util.List;

@Path("history/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookHistoryResource {

    @Inject
    BookHistoryRepository bookHistoryRepository;

    @GET
    @Path("{bookId}")
    public List<BookResponse> getHistory(@PathParam("bookId") long bookId) {
        return bookHistoryRepository.getHistory(bookId)
                .stream()
                .map(BookResponse::fromEntity)
                .toList();
    }

    @GET
    @Path("bookId/{bookId}")
    public List<Book> getEntriesByIdAndRevType(@PathParam("bookId") long bookId, @DefaultValue("ADD") @QueryParam("revtype") RevisionType revtype) {
        return bookHistoryRepository.getEntriesByIdAndRevType(bookId, revtype);
    }

    @GET
    @Path("timestamp/{timestamp}")
    public List<Book> getByTimestamp(@PathParam("timestamp") Instant timestamp) {
        return bookHistoryRepository.getByTimestamp(timestamp);
    }

    @GET
    @Path("title/{title}")
    public List<Book> getByTitle(@PathParam("title") String title) {
        return bookHistoryRepository.getByTitle(title);
    }

    @GET
    @Path("title/{title}/ordered")
    public List<Book> getByTitleOrdered(@PathParam("title") String title) {
        return bookHistoryRepository.getByTitleOrderByPublicationYear(title);
    }
}
