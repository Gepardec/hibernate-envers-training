package com.gepardec.bookshop.persistence.repository.history;

import com.gepardec.bookshop.persistence.entity.Author;
import com.gepardec.bookshop.persistence.entity.Revision;
import com.gepardec.bookshop.rest.dto.AuthorRevisionDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AuthorHistoryRepository {

    @Inject
    EntityManager em;

    private AuditReader auditReader() {
        return AuditReaderFactory.get(em);
    }

    public List<Author> getHistory(Long authorId) {
        AuditReader auditReader = AuditReaderFactory.get(em);

        List<Number> revisions = auditReader.getRevisions(Author.class, authorId);

        return revisions.stream()
                .map(rev -> auditReader.find(Author.class, authorId, rev))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<AuthorRevisionDto> getWithRevisions(Long authorId) {
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
                            // TODO 3: add username
                            revType
                    );
                })
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<AuthorRevisionDto> findRevisionsByUser(String username) {
        // TODO 7: create author query

        List<AuthorRevisionDto> revisions = new ArrayList<>();
        // TODO 7: create AuthorRevisionDto

        return revisions;
    }

    @SuppressWarnings("unchecked")
    public List<AuthorRevisionDto> findRevisionsByName(String authorName) {
        // TODO 6: create vertical author name query

        return null;
    }
}
