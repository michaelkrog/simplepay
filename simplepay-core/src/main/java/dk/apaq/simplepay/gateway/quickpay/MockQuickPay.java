package dk.apaq.simplepay.gateway.quickpay;

import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;



/**
 *
 * @author krog
 */
public class MockQuickPay implements PaymentGateway{

    
    private boolean authorized;
    private boolean captured;
    private long authAmount=Long.MAX_VALUE;
    private long captureAmount;
    private String transactionId;
    private String authCurrency;
    private String authOrderId;
    private String legalTransactionId;

    @Override
    public void setMerchantId(String merchantId) {
        
    }

    @Override
    public void setMerchantSecret(String merchantSecret) {
    
    }
    
    
    @Override
    public void capture(long amountInCents, String transactionId) {
        if(captured) {
            throw new PaymentException("Already captured");
        }
        
        if(amountInCents > authAmount) {
            throw new PaymentException("Not allowed to capture more than authorized.");
        }
        
        captured = true;
        captureAmount = amountInCents;
    }

    @Override
    public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        if(legalTransactionId != null && !legalTransactionId.equals(transactionId)) {
            throw new PaymentException("Not legal transactionid [transationId="+transactionId+"]");
        }
        authorized = true;
        authOrderId = orderNumber;
        authAmount = amountInCents;
        authCurrency = currency;
        this.transactionId = transactionId;
        
        if(autocapture) {
            capture(amountInCents, transactionId);
        }
    }

    public String getLegalTransactionId() {
        return legalTransactionId;
    }

    public void setLegalTransactionId(String legalTransactionId) {
        this.legalTransactionId = legalTransactionId;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public boolean isCaptured() {
        return captured;
    }

    public long getAuthAmount() {
        return authAmount;
    }
    

    public String getAuthCurrency() {
        return authCurrency;
    }

    public String getAuthOrderId() {
        return authOrderId;
    }

    public long getCaptureAmount() {
        return captureAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void reset() {
        this.authAmount = 0;
        this.authCurrency = null;
        this.authOrderId = null;
        this.authorized = false;
        this.captureAmount = 0;
        this.captured = false;
        this.transactionId = null;
        this.legalTransactionId = null;
    }
    
    @Override
    public void renew(long amountInCents, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refund(long amountInCents, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PaymentInformation getPaymentInformation(String transactionId) {
        return null;
    }
    
}
