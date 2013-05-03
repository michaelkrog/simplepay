/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.model;

import java.util.Date;

/**
 * Javadoc
 */
public abstract class BaseEntityWithoutGeneratedId implements BaseEntity {
    private String _id;
    
    protected Date dateChanged;
    protected Date dateCreated;

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public void setId(String _id) {
        this._id = _id;
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
        return _id == null;
    }
}
