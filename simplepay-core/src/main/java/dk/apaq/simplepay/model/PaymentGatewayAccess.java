package dk.apaq.simplepay.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;

import dk.apaq.framework.common.beans.finance.PaymentIntrument;
import dk.apaq.simplepay.gateway.EPaymentGateway;

/**
 *
 * @author krog
 */
@Entity
public class PaymentGatewayAccess extends BaseEntity {

    private EPaymentGateway paymentGatewayType;
    private String acquirerRefId;
    private List<PaymentIntrument> specificValidInstruments = new ArrayList<PaymentIntrument>();

    protected PaymentGatewayAccess() {
    }

    public PaymentGatewayAccess(EPaymentGateway paymentGatewayType, String acquirerRefId) {
        this.paymentGatewayType = paymentGatewayType;
        this.acquirerRefId = acquirerRefId;
    }

    public EPaymentGateway getPaymentGatewayType() {
        return paymentGatewayType;
    }

    public void setPaymentGatewayType(EPaymentGateway paymentGatewayType) {
        this.paymentGatewayType = paymentGatewayType;
    }

    /**
     * Retrieves the reference id used to identifiy the merchant when communicating with the gateway. (Often called the 'merchant id').
     * @return The reference id.
     */
    public String getAcquirerRefId() {
        return acquirerRefId;
    }

    /**
     * Set the reference id.
     * @param acquirerRefId The id
     */
    public void setAcquirerRefId(String acquirerRefId) {
        this.acquirerRefId = acquirerRefId;
    }

    /**
     * Retrieves a list of valid payment instruments for this PaymentGatewayAccess.
     * @return The list of instruments or an empty list if this supports all instruments.
     */
    public List<PaymentIntrument> getSpecificValidInstruments() {
        return Collections.unmodifiableList(specificValidInstruments);
    }

    /**
     * Sets the list of specific valid payments instruments for this access or an empty list
     * if this access is valid for all payments instruments.
     * @param validInstruments The list of instruments or an empty list.
     */
    public void setSpecificValidInstruments(List<PaymentIntrument> validInstruments) {
        this.specificValidInstruments = new ArrayList<PaymentIntrument>(validInstruments);
    }
    
}
