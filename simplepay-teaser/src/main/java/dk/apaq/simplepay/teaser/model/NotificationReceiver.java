package dk.apaq.simplepay.teaser.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class NotificationReceiver {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    @Column(name = "COPY_SCRIPT_ID", length = 40, nullable = false)
    private String id;
    private String mail;
    private String locale;
    private String ip;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    public NotificationReceiver() {
    }

    public NotificationReceiver(String mail, String locale, String ip) {
        this.mail = mail;
        this.locale = locale;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
