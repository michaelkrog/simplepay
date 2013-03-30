package dk.apaq.simplepay.client.model;

import java.util.Date;
import java.util.Map;


/**
 *
 * @author krog
 */
public class Token {

    private String id;
    
    protected Date dateChanged;
    protected Date dateCreated;
    private ETokenPurpose purpose = ETokenPurpose.SinglePayment;
    private boolean expired = false;
    private boolean test = false;
    
    private Map tokenData;
    
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

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public ETokenPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(ETokenPurpose purpose) {
        this.purpose = purpose;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public Map getData() {
        return tokenData;
    }

    public void setData(Map tokenData) {
        this.tokenData = tokenData;
    }
    
    
}
