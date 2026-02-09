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
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;

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
                            rev.getUsername(),
                            revType
                    );
                })
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<AuthorRevisionDto> findRevisionsByUser(String username) {
        List<Object[]> results = auditReader()
                .createQuery()
                .forRevisionsOfEntity(Author.class, false, false)
                .add(AuditEntity.revisionProperty("username").eq(username))
                .getResultList();

        List<AuthorRevisionDto> revisions = new ArrayList<>();
        for (Object[] row : results) {
            Author author = (Author) row[0];
            Revision rev = (Revision) row[1];
            RevisionType type = (RevisionType) row[2];
            revisions.add(new AuthorRevisionDto(
                    author.getId(),
                    author.getName(),
                    rev.getId(),
                    rev.getTimestamp(),
                    rev.getUsername(),
                    type
            ));
        }

        return revisions;
    }

    @SuppressWarnings("unchecked")
    public List<AuthorRevisionDto> findRevisionsByName(String authorName) {
        AuditQuery query = auditReader().createQuery().forRevisionsOfEntity(Author.class, false, true);
        List<Object[]> rows = query.add(AuditEntity.property("name").ilike(authorName, MatchMode.ANYWHERE))
                .addOrder(AuditEntity.revisionType().desc())
                .getResultList();

        return rows.stream()
                .map(row -> {
                    Author author = (Author) row[0];
                    Revision rev = (Revision) row[1];
                    RevisionType revType = (RevisionType) row[2];

                    return new AuthorRevisionDto(
                            author.getId(),
                            author.getName(),
                            rev.getId(),
                            rev.getTimestamp(),
                            rev.getUsername(),
                            revType
                    );
                })
                .toList();
    }
}
