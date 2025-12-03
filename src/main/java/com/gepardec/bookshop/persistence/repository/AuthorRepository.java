package com.gepardec.bookshop.persistence.repository;

import com.gepardec.bookshop.persistence.entity.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AuthorRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Author create(Author author) {
        em.persist(author);
        return author;
    }

    public Author findById(Long id) {
        return em.find(Author.class, id);
    }

    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }
}
