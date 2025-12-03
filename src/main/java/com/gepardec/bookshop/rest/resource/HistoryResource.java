package com.gepardec.bookshop.rest.resource;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.repository.HistoryRepository;
import com.gepardec.bookshop.rest.dto.AuthorRevisionDto;
import com.gepardec.bookshop.rest.dto.BookResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoryResource {

    @Inject
    HistoryRepository historyRepository;

    @GET
    @Path("book/{bookId}")
    public List<BookResponse> getBookHistory(@PathParam("bookId") long bookId) {
        return historyRepository.getBookHistory(bookId)
                .stream()
                .map(BookResponse::fromEntity)
                .toList();
    }

    @GET
    @Path("author/{authorId}")
    public List<Author> getAuthorHistory(@PathParam("authorId") long authorId) {
        return historyRepository.getAuthorHistory(authorId);
    }

    @GET
    @Path("author/{authorId}/full")
    public List<AuthorRevisionDto> getAuthorHistoryFull(@PathParam("authorId") long authorId) {
        return historyRepository.getAuthorHistoryFull(authorId);
    }

    @GET
    @Path("author/{username}")
    public List<Author> getAuthorHistoryByUsername(@PathParam("username") String username) {
        return historyRepository.findAuthorRevisionsByUser(username);
    }
}
