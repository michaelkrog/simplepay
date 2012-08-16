package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.common.TransactionStatus;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author krog
 */
public class PaymentInformation {
    
    private final TransactionStatus transationStatus;
    private final List<HistoryEntry> history;
    private final String orderNumber;
    private final int amount;
    private final String currency;
    private final String gatewayStatus;
    private final String merchantId;
    private final String merchantEmail;
    private final String transactionId;
    private final PaymentMethod cardType;
    
    public class HistoryEntry {
        private String type;
        private int amount;
        private PaymentInformation status;
        private Date timestamp;
        private String gatewayStatus;

        public HistoryEntry(String type, int amount, PaymentInformation status, Date timestamp, String gatewayStatus) {
            this.type = type;
            this.amount = amount;
            this.status = status;
            this.timestamp = timestamp;
            this.gatewayStatus = gatewayStatus;
        }

        public int getAmount() {
            return amount;
        }

        public String getGatewayStatus() {
            return gatewayStatus;
        }

        public PaymentInformation getStatus() {
            return status;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public String getType() {
            return type;
        }
    }

    public PaymentInformation(TransactionStatus transactionStatus, List<HistoryEntry> history, String orderNumber, int amount, String currency, 
            String gatewayStatus, String merchantId, String merchantEmail, String transactionId, PaymentMethod cardType) {
        this.transationStatus = transactionStatus;
        this.history = history;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.currency = currency;
        this.gatewayStatus = gatewayStatus;
        this.merchantId = merchantId;
        this.merchantEmail = merchantEmail;
        this.transactionId = transactionId;
        this.cardType = cardType;
        
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getGatewayStatus() {
        return gatewayStatus;
    }

    public List<HistoryEntry> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public TransactionStatus getTransationStatus() {
        return transationStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }
    
    
    
}
