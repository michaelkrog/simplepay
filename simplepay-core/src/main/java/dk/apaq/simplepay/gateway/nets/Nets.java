package dk.apaq.simplepay.gateway.nets;

import java.io.IOException;
import dk.apaq.nets.payment.Address;
import dk.apaq.nets.payment.Card;
import dk.apaq.nets.payment.Merchant;
import dk.apaq.nets.payment.NetsException;
import dk.apaq.simplepay.gateway.AbstractPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.apache.commons.lang.Validate;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public class Nets extends AbstractPaymentGateway implements IPaymentGateway {

    private final dk.apaq.nets.payment.Nets api;

    public Nets(dk.apaq.nets.payment.Nets api) {
        this.api = api;
    }
    
    public void authorize(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, dk.apaq.simplepay.model.Card sCard, Money money, 
            String orderId, String terminalId, ETokenPurpose purpose) {
        try {
            Validate.notNull(orderId, "orderId is null");
            Validate.notNull(money, "money is null");
            
            Card card = convertCard(sCard);
            Merchant merchant = merchantFromMerchantAndAccess(sMerchant, access);
            api.authorize(merchant, card, money, orderId, purpose == ETokenPurpose.RecurringPayment, false, false, false);
            throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            throw new PaymentException("Error communicating with Nets.", ex);
        } catch (NetsException ex) {
            throw new PaymentException("Nets returned an error related to the authorization.", ex);
        }
    }

    public void cancel(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId) {
        try {
            Merchant merchant = merchantFromMerchantAndAccess(sMerchant, access);
            api.reverse(merchant, orderId);
        } catch (IOException ex) {
            throw new PaymentException("Error communicating with Nets.", ex);
        } catch (NetsException ex) {
            throw new PaymentException("Nets returned an error related to the cancelation.", ex);
        }
    }

    public void capture(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refund(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void renew(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private Card convertCard(dk.apaq.simplepay.model.Card card) {
        Validate.notNull(card, "card is null");
        return new Card(card.getName(), card.getNumber(), card.getExpMonth(), card.getExpYear(), card.getCvd());
    }
    
    private Merchant merchantFromMerchantAndAccess(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access) {
        Validate.notNull(access, "access is null");
        Validate.notNull(sMerchant, "merchant is null");
        Address address = new Address(sMerchant.getStreet(), sMerchant.getPostalCode(), sMerchant.getCity(), sMerchant.getCountryCode());
        return new Merchant(access.getAcquirerRefId(), sMerchant.getName(), address);
        
    }
}
