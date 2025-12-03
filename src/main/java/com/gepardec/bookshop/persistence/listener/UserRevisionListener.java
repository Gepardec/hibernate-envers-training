package com.gepardec.bookshop.persistence.listener;

import com.gepardec.bookshop.persistence.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Revision rev = (Revision) revisionEntity;
        String username = UsernameHolder.get();
        if (username == null) {
            username = "unknown";
        }
        rev.setUsername(username);
    }
}