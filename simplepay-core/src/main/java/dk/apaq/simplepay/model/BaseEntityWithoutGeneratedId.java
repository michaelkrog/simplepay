/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.model;

import java.util.Date;

/**
 * Javadoc
 */
public abstract class BaseEntityWithoutGeneratedId implements BaseEntity {
    private String id;
    
    protected Date dateChanged;
    protected Date dateCreated;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateChanged() {
        return dateChanged;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
