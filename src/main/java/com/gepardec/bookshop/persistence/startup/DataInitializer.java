package com.gepardec.bookshop.persistence.startup;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.entity.Book;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitializer {

    @Inject
    EntityManager em;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        // avoid double-inserts on restart in dev:
        if (!em.createQuery("select a from Author a", Author.class)
                .getResultList().isEmpty()) {
            return;
        }

        createInitialData();

        updateAuthor();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void createInitialData() {
        Author king = new Author();
        king.setName("Stephen King");
        king.setEmail("stephen.king@example.com");
        king.setInternalNotes("My secret notes.");
        em.persist(king);

        Author rowling = new Author();
        rowling.setName("J.K. Rowling");
        rowling.setEmail("jk.rowling@example.com");
        em.persist(rowling);

        Book shining = new Book();
        shining.setTitle("The Shining");
        shining.setIsbn("9780307743657");
        shining.setPublicationYear(1977);
        shining.setAuthor(king);
        em.persist(shining);

        Book hp = new Book();
        hp.setTitle("Harry Potter and the Philosopher's Stone");
        hp.setIsbn("9780747532699");
        hp.setPublicationYear(1997);
        hp.setAuthor(rowling);
        em.persist(hp);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateAuthor() {
        Author author = em.find(Author.class, 1);
        author.setName("Stephen Martin King");
        em.merge(author);
    }
}
