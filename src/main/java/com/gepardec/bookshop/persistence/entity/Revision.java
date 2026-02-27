package com.gepardec.bookshop.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.RevisionMapping;

@Entity
@Table(name = "REVISION")
public class Revision extends RevisionMapping {

    // TODO 3: add username
}
