package dk.apaq.simplepay.model;

import java.util.Date;

/**
 *
 * @author krog
 */
public abstract class BaseEntityWithGeneratedId implements BaseEntity {

    private String _id;
    
    protected Date dateChanged;
    protected Date dateCreated;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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
        return _id == null;
    }
    
    

}
