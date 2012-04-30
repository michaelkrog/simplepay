package dk.apaq.simplepay.model;

import java.io.Serializable;

/**
 *
 * @author michael
 */
public interface Event extends Serializable {
    
    
    public Merchant getMerchant() ;

    public void setMerchant(Merchant merchant) ;

}
