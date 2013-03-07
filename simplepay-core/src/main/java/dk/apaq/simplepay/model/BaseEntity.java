package dk.apaq.simplepay.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Javadoc
 */
public interface BaseEntity extends Serializable {

    Date getDateChanged();

    Date getDateCreated();

    String getId();

    void setDateChanged(Date dateChanged);

    void setDateCreated(Date dateCreated);

    void setId(String _id);
    
}
