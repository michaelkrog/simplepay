package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;

/**
 *
 * @author krog
 */
public class Transaction {
    private String id;
    private long authorizedAmount;
    private long capturedAmount;
    private long refundedAmount;
    private String orderNumber;
    private Merchant merchant;
    private String currency;
    private String description;
    private String gatewayTransactionId;
    private PaymentGatewayType gatewayType;
    private boolean captured;
    private boolean refunded;
}
