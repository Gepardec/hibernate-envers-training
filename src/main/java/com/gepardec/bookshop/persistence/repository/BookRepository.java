package com.gepardec.bookshop.persistence.repository;

import com.gepardec.bookshop.persistence.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BookRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Book create(Book book) {
        em.persist(book);
        return book;
    }

    public Book findById(Long id) {
        return em.find(Book.class, id);
    }

    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class)
                .getResultList();
    }

    @Transactional
    public void delete(Long id) {
        Book b = em.find(Book.class, id);
        if (b != null) {
            em.remove(b);
        }
    }
}
