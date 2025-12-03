package com.gepardec.bookshop.persistence.repository;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.entity.Book;
import com.gepardec.bookshop.persistence.entity.Revision;
import com.gepardec.bookshop.rest.dto.AuthorRevisionDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

import java.util.List;

@ApplicationScoped
public class HistoryRepository {

    @Inject
    EntityManager em;

    private AuditReader auditReader() {
        return AuditReaderFactory.get(em);
    }

    public List<Book> getBookHistory(Long bookId) {
        List<Number> revs = auditReader().getRevisions(Book.class, bookId);

        return revs.stream()
                .map(rev -> auditReader().find(Book.class, bookId, rev))
                .toList();
    }

    public List<Author> getAuthorHistory(Long authorId) {
        AuditReader auditReader = AuditReaderFactory.get(em);

        List<Number> revisions = auditReader.getRevisions(Author.class, authorId);

        return revisions.stream()
                .map(rev -> auditReader.find(Author.class, authorId, rev))
                .toList();
    }

    public List<AuthorRevisionDto> getAuthorHistoryFull(Long authorId) {


        List<Object[]> rows = auditReader().createQuery()
                .forRevisionsOfEntity(Author.class, false, true)
                .add(AuditEntity.id().eq(authorId))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        return rows.stream()
                .map(row -> {
                    Author author = (Author) row[0];
                    Revision rev = (Revision) row[1];
                    RevisionType revType = (RevisionType) row[2];

                    return new AuthorRevisionDto(
                            author.getId(),
                            author.getName(),
                            rev.getId(),           // Revision Number
                            rev.getTimestamp(),
                            rev.getUsername(),
                            revType
                    );
                })
                .toList();
    }

    public List<Author> findAuthorRevisionsByUser(String username) {
        return auditReader()
                .createQuery()
                .forRevisionsOfEntity(Author.class, false, true)
                .add(AuditEntity.revisionProperty("username").eq(username))
                .getResultList();
    }
}
