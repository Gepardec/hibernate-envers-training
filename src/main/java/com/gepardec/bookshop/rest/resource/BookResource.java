package com.gepardec.bookshop.rest.resource;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.entity.Book;
import com.gepardec.bookshop.persistence.repository.AuthorRepository;
import com.gepardec.bookshop.persistence.repository.BookRepository;
import com.gepardec.bookshop.rest.dto.BookRequest;
import com.gepardec.bookshop.rest.dto.BookResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookRepository bookRepo;

    @Inject
    AuthorRepository authorRepo;

    @GET
    public List<BookResponse> list() {
        return bookRepo.findAll()
                .stream()
                .map(BookResponse::fromEntity)
                .toList();
    }

    @GET
    @Path("{id}")
    public BookResponse get(@PathParam("id") Long id) {
        Book book = bookRepo.findById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        return BookResponse.fromEntity(book);
    }

    @POST
    @Transactional
    public Response create(BookRequest request) {
        Author author = authorRepo.findById(request.getAuthorId());
        if (author == null) {
            throw new NotFoundException("Author not found: " + request.getAuthorId());
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublicationYear(request.getPublicationYear());
        book.setAuthor(author);

        bookRepo.create(book);

        return Response.created(URI.create("/books/" + book.getId()))
                .entity(BookResponse.fromEntity(book))
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public BookResponse update(@PathParam("id") Long id, BookRequest request) {
        Book book = bookRepo.findById(id);
        if (book == null) {
            throw new NotFoundException();
        }

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }
        if (request.getPublicationYear() != null) {
            book.setPublicationYear(request.getPublicationYear());
        }
        if (request.getAuthorId() != null) {
            Author author = authorRepo.findById(request.getAuthorId());
            if (author == null) {
                throw new NotFoundException("Author not found: " + request.getAuthorId());
            }
            book.setAuthor(author);
        }

        return BookResponse.fromEntity(book);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        bookRepo.delete(id);
    }
}
