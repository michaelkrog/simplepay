/*
 * No one but emploeyees at Apaq is allowed to use this code.
 * It may not be copied or used in any context unless by Apaq.
 */
package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.CardType;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author krog
 */
public interface Token extends Serializable{
    
    public String getId();
    
    public Merchant getMerchant() ;

    public void setMerchant(Merchant merchant) ;

    public String getCardExpires();

    public String getCardNumberTruncated();

    public CardType getCardType() ;
    
    public Date getDateChanged();

    public Date getDateCreated();

    public String getGatewayTransactionId();

    public PaymentGatewayType getGatewayType();



    public void setCardExpires(String cardExpires);

    public void setCardNumberTruncated(String cardNumberTruncated);

    public void setCardType(CardType cardType);

    public void setDateChanged(Date dateChanged);

    public void setDateCreated(Date dateCreated);

    public void setGatewayTransactionId(String gatewayTransactionId);

    public void setGatewayType(PaymentGatewayType gatewayType);

 
}
