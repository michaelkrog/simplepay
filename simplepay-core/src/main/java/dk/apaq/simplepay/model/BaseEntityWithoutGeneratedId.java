/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Javadoc
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntityWithoutGeneratedId implements BaseEntity {
    @Id
    private String _id;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date dateChanged;
    @Temporal(value = TemporalType.TIMESTAMP)
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

}
