package dk.apaq.simplepay.model;

import javax.persistence.Entity;

import dk.apaq.simplepay.gateway.EPaymentGateway;

/**
 *
 * @author krog
 */
@Entity
public class PaymentGatewayAccess extends BaseEntity {

    private EPaymentGateway paymentGatewayType;
    private String acquirerRefId;
    private String acquirerApiKey;

    protected PaymentGatewayAccess() {
    }

    public PaymentGatewayAccess(EPaymentGateway paymentGatewayType, String acquirerRefId, String acquirerApiKey) {
        this.paymentGatewayType = paymentGatewayType;
        this.acquirerRefId = acquirerRefId;
        this.acquirerApiKey = acquirerApiKey;
    }

    public EPaymentGateway getPaymentGatewayType() {
        return paymentGatewayType;
    }

    public void setPaymentGatewayType(EPaymentGateway paymentGatewayType) {
        this.paymentGatewayType = paymentGatewayType;
    }

    public String getAcquirerApiKey() {
        return acquirerApiKey;
    }

    public void setAcquirerApiKey(String acquirerApiKey) {
        this.acquirerApiKey = acquirerApiKey;
    }

    public String getAcquirerRefId() {
        return acquirerRefId;
    }

    public void setAcquirerRefId(String acquirerRefId) {
        this.acquirerRefId = acquirerRefId;
    }
}
