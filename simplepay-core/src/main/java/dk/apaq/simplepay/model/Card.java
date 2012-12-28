package dk.apaq.simplepay.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import dk.apaq.simplepay.common.EPaymentIntrument;
import dk.apaq.simplepay.common.EPaymentType;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author krog
 */
@Embeddable
public class Card {

    @JsonIgnore
    private String encryptedNumber;
    private int expMonth;
    private int expYear;
    private String name;
    @JsonIgnore
    private String encryptedCvd;
    private EPaymentIntrument type;
    private String last4;
    private boolean valid;

    protected Card() {
    }

    public Card(String encryptedNumber, String last4, int expMonth, int expYear, String encryptedCvd, boolean valid, EPaymentIntrument type) {
        this(null, encryptedNumber, last4, expMonth, expYear, encryptedCvd, valid, type);
    }

    public Card(String name, String encryptedNumber, String last4, int expMonth, int expYear, String encryptedCvd, boolean valid,
            EPaymentIntrument type) {
        Validate.isTrue(expMonth >= 1 && expMonth <= 12, "expMonth must be within the range of 1-12.");
        Validate.isTrue(expYear >= 2000 && expMonth <= 2100, "expMonth must be within the range of 2000-2100.");
        Validate.notNull(encryptedNumber, "encryptedNumber is null.");
        Validate.notNull(last4, "last4 is null.");
        Validate.isTrue(last4.length() == 4, "last4 must have length of 4.");
        Validate.notNull(encryptedCvd, "encryptedCvd is null.");
        Validate.notNull(type, "type is null.");

        this.name = name;
        this.encryptedNumber = encryptedNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.encryptedCvd = encryptedCvd;
        this.type = type;
        this.last4 = last4;
    }

    public boolean isValid() {
        return valid;
    }

    public String getEncryptedCvd() {
        return encryptedCvd;
    }

    public String getEncryptedNumber() {
        return encryptedNumber;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public String getLast4() {
        return last4;
    }

    public String getName() {
        return name;
    }

    public EPaymentIntrument getType() {
        return type;
    }
}
