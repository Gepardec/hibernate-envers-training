package com.gepardec.bookshop.persistence.repository.history;

import com.gepardec.bookshop.persistence.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class BookHistoryRepository {

    @Inject
    EntityManager em;

    private AuditReader auditReader() {
        return AuditReaderFactory.get(em);
    }

    public List<Book> getHistory(Long bookId) {
        List<Number> revs = auditReader().getRevisions(Book.class, bookId);

        return revs.stream()
                .map(rev -> auditReader().find(Book.class, bookId, rev))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<Book> getEntriesByIdAndRevType(long bookId, RevisionType revType) {
        return auditReader()
                .createQuery().forRevisionsOfEntity(Book.class, true, true)
                .add(AuditEntity.property("id").eq(bookId))
                .add(AuditEntity.revisionType().eq(revType))
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Book> getByTimestamp(Instant timestamp) {
        Number revision = auditReader().getRevisionNumberForDate(timestamp);
        return auditReader().createQuery().forEntitiesAtRevision(Book.class, revision).getResultList();
    }
}
