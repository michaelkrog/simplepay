package dk.apaq.simplepay.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author michael
 */
public interface Event extends Serializable {
    
    
    public Merchant getMerchant() ;

    public void setMerchant(Merchant merchant) ;

    public Date getTimestamp();
}
