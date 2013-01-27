package dk.apaq.simplepay.gateway.nets;

import java.io.IOException;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.nets.payment.Address;
import dk.apaq.nets.payment.Merchant;
import dk.apaq.nets.payment.Nets;
import dk.apaq.nets.payment.NetsException;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.apache.commons.lang.Validate;
import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public class NetsGateway implements IPaymentGateway {

    private final Nets api;
    
    /**
     * Constructs a new instance of the NetsGateway
     * @param api The api instance for use by the gateway instance.
     */
    public NetsGateway(Nets api) {
        this.api = api;
    }
    
    
    /**
     * @{@inheritDoc}
     */
    @Override
    public void authorize(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, Card card, Money money, 
            String orderId, String terminalId, ETokenPurpose purpose) {
        try {
            Validate.notNull(orderId, "orderId is null");
            Validate.notNull(money, "money is null");
            
            Merchant merchant = merchantFromMerchantAndAccess(sMerchant, access);
            api.authorize(merchant, card, money, orderId, purpose == ETokenPurpose.RecurringPayment, false, false, false);
            
        } catch (IOException ex) {
            throw new PaymentException("Error communicating with Nets.", ex);
        } catch (NetsException ex) {
            throw new PaymentException("Nets returned an error related to the authorization.", ex);
        }
    }

    /**
     * @{@inheritDoc}
     */
    @Override
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

    /**
     * @{@inheritDoc}
     */
    @Override
    public void capture(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, 
            long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public void refund(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, 
            long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public void renew(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access, String transactionId, String orderId, 
            long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private Merchant merchantFromMerchantAndAccess(dk.apaq.simplepay.model.Merchant sMerchant, PaymentGatewayAccess access) {
        Validate.notNull(access, "access is null");
        Validate.notNull(sMerchant, "merchant is null");
        Address address = new Address(sMerchant.getStreet(), sMerchant.getPostalCode(), sMerchant.getCity(), sMerchant.getCountryCode());
        return new Merchant(access.getAcquirerRefId(), sMerchant.getName(), address);
        
    }
}