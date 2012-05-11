package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public class TransactionCrud extends EntityManagerCrudForSpring<String, Transaction> implements ITransactionCrud {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    public TransactionCrud(EntityManager em) {
        super(em, Transaction.class);
        
    }

    @Transactional
    public Transaction createNew(Token token) {
        Transaction transaction = new Transaction(token);
        return createAndRead(transaction);
    }

    @Transactional
    public Transaction charge(Transaction transaction, long amount) {
        transaction = read(transaction.getId());
        transaction.setCapturedAmount(amount);
        transaction.setStatus(TransactionStatus.Charged);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getMerchant(), transaction.getToken().getGatewayType());
        gateway.capture(transaction.getToken(), amount);
        
        return update(transaction);
    }

    @Transactional
    public Transaction cancel(Transaction transaction) {
        transaction = read(transaction.getId());
        transaction.setStatus(TransactionStatus.Cancelled);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getMerchant(), transaction.getToken().getGatewayType());
        gateway.cancel(transaction.getToken());
        
        return update(transaction);
    }

    @Transactional
    public Transaction refund(Transaction transaction, long amount) {
        transaction = read(transaction.getId());
        transaction.setRefundedAmount(amount);
        transaction.setStatus(TransactionStatus.Refunded);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getMerchant(), transaction.getToken().getGatewayType());
        gateway.capture(transaction.getToken(), amount);
        
        return update(transaction);
    }
    
    
    
}
