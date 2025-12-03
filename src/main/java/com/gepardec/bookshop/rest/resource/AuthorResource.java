package com.gepardec.bookshop.rest.resource;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.repository.AuthorRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/author")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @Inject
    AuthorRepository authorRepo;

    @GET
    public List<Author> list() {
        return authorRepo.findAll();
    }

    @GET
    @Path("{id}")
    public Author get(@PathParam("id") Long id) {
        Author author = authorRepo.findById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        return author;
    }

    @POST
    public Response create(Author author) {
        authorRepo.create(author);
        return Response.created(URI.create("/authors/" + author.getId()))
                .entity(author)
                .build();
    }
}
