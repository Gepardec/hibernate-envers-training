package com.gepardec.bookshop.persistence.listener;

import com.gepardec.bookshop.persistence.entity.Revision;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import org.hibernate.envers.RevisionListener;

@ApplicationScoped
public class UserRevisionListener implements RevisionListener {

    @Inject
    HttpHeaders httpHeaders;

    @Override
    public void newRevision(Object revisionEntity) {
        Revision revision = (Revision) revisionEntity;

        if (httpHeaders == null) {
            revision.setUsername("unknown");
            return;
        }

        String user = httpHeaders.getHeaderString("username");
        if (user == null) {
            revision.setUsername("unknown");
            return;
        }

        revision.setUsername(user);
    }
}