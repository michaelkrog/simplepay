package dk.apaq.simplepay.crud;

import dk.apaq.crud.jpa.EntityManagerCrudForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
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
    
    @Autowired
    private IPayService service;
    
    public TransactionCrud(EntityManager em) {
        super(em, Transaction.class);
        
    }

    @Transactional
    public Transaction createNew(Token token) {
        Transaction transaction = new Transaction(/*token.getId()*/);
        transaction = createAndRead(transaction);
        
        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), TransactionStatus.Authorized, RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TransactionEvent.class).createAndRead(evt);
        
        return transaction;
    }

    @Transactional
    public Transaction charge(Transaction transaction, long amount) {
        transaction = read(transaction.getId());
        transaction.setAmountCharged(amount);
        transaction.setStatus(TransactionStatus.Charged);
        
        PaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.capture(transaction.getToken(), amount);
        
        transaction = update(transaction);
        
        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), TransactionStatus.Charged, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).createAndRead(evt);
        
        return transaction;
    }

    @Transactional
    public Transaction cancel(Transaction transaction) {
        transaction = read(transaction.getId());
        transaction.setStatus(TransactionStatus.Cancelled);
        
        PaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.cancel(transaction.getToken());
        
        transaction = update(transaction);
        
        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), TransactionStatus.Cancelled, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).createAndRead(evt);
        
        return transaction;
    }

    @Transactional
    public Transaction refund(Transaction transaction, long amount) {
        transaction = read(transaction.getId());
        transaction.setAmountRefunded(amount);
        transaction.setStatus(TransactionStatus.Refunded);
        
        PaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.refund(transaction.getToken(), amount);
        
        transaction = update(transaction);
        
        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), TransactionStatus.Refunded, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).createAndRead(evt);
        
        return transaction;
    }
    
    
    
}
