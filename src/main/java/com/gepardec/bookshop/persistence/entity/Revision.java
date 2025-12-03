package com.gepardec.bookshop.persistence.entity;

//import com.gepardec.bookshop.persistence.listener.UserRevisionListener;
import com.gepardec.bookshop.persistence.listener.UserRevisionListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionMapping;

import java.util.Objects;

@Entity
@RevisionEntity(UserRevisionListener.class)
@Table(name = "REVISION")
public class Revision extends RevisionMapping {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Revision revision)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(username, revision.username);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(username);
        return result;
    }
}
