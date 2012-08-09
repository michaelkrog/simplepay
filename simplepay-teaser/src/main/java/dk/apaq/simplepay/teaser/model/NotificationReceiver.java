package dk.apaq.simplepay.teaser.model;

import java.util.Date;

public class NotificationReceiver {
    
    private String id;
    private String mail;
    private Date timestamp = new Date();

    public NotificationReceiver(String mail) {
        this.mail = mail;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getTimestamp() {
        return timestamp;
    }   
}
