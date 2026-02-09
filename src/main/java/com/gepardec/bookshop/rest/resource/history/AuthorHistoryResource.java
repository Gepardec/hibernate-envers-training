package com.gepardec.bookshop.rest.resource.history;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.repository.history.AuthorHistoryRepository;
import com.gepardec.bookshop.rest.dto.AuthorRevisionDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("history/author")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorHistoryResource {

    @Inject
    AuthorHistoryRepository authorHistoryRepository;

    @GET
    @Path("{authorId}")
    public List<Author> getHistory(@PathParam("authorId") long authorId) {
        return authorHistoryRepository.getHistory(authorId);
    }

    @GET
    @Path("{authorId}/full")
    public List<AuthorRevisionDto> getWithRevisions(@PathParam("authorId") long authorId) {
        return authorHistoryRepository.getWithRevisions(authorId);
    }

    @GET
    @Path("username/{username}")
    public List<AuthorRevisionDto> findRevisionsByUser(@PathParam("username") String username) {
        return authorHistoryRepository.findRevisionsByUser(username);
    }

    @GET
    @Path("authorName/{authorName}")
    public List<AuthorRevisionDto> findRevisionsByName(@PathParam("authorName") String authorName) {
        return authorHistoryRepository.findRevisionsByName(authorName);
    }
}
