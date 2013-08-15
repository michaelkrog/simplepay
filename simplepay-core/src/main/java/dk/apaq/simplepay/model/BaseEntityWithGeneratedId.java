package dk.apaq.simplepay.model;

import java.util.Date;

/**
 *
 * @author krog
 */
public abstract class BaseEntityWithGeneratedId implements BaseEntity {

    private String id;
    
    protected Date dateChanged;
    protected Date dateCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
    
    

}
