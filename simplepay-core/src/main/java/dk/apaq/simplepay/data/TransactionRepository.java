package dk.apaq.simplepay.data;

import javax.persistence.EntityManager;

import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public class TransactionRepository extends EntityManagerRepositoryForSpring<Transaction, String> implements ITransactionRepository {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    private IPayService service;

    /**
     * Constructs an instance of Transaction Repository.
     * @param em The entitymanager that holds transactions.
     */
    public TransactionRepository(EntityManager em) {
        super(em, Transaction.class);

    }

    @Transactional
    @Override
    public Transaction createNew(Token token, String refId, Money money) {
        //Get preferred gateway
        EPaymentGateway gatewayType = token.getMerchant().getPreferredPaymentGateway(token.getData(), money);
        if (gatewayType == null) {
            throw new PaymentException("Unable to retrieve preferred payment type for merchant. [Merchant=" + token.getMerchant().getId() + "]");
        }

        Transaction transaction = new Transaction(token.getId(), refId, money, gatewayType);


        //Create gateway
        IPaymentGateway gateway = gatewayManager.createPaymentGateway(gatewayType);

        //authorize payment
        gateway.authorize(token, money, refId, "");

        //Store authorized transaction
        transaction = save(transaction);

        //if token only for single usage then mark it expired
        if (token.getPurpose() == ETokenPurpose.SinglePayment) {
            service.getTokens(token.getMerchant()).markExpired(token);
        }

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Authorized,
                RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    @Override
    public Transaction charge(Transaction transaction, long amount) {
        transaction = loadTransaction(transaction);
        Token token = loadtoken(transaction);
        
        transaction.setAmountCharged(amount);
        transaction.setStatus(ETransactionStatus.Charged);

        IPaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getGatewayType());
        gateway.capture(token, amount);

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Charged, 
                RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    @Override
    public Transaction cancel(Transaction transaction) {
        transaction = loadTransaction(transaction);
        Token token = loadtoken(transaction);
        transaction.setStatus(ETransactionStatus.Cancelled);

        IPaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getGatewayType());
        gateway.cancel(token);

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Cancelled, 
                RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    @Override
    public Transaction refund(Transaction transaction, long amount) {
        transaction = loadTransaction(transaction);
        Token token = loadtoken(transaction);
        transaction.setAmountRefunded(amount);
        transaction.setStatus(ETransactionStatus.Refunded);

        IPaymentGateway gateway = gatewayManager.createPaymentGateway(transaction.getGatewayType());
        gateway.refund(token, amount);

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Refunded, 
                RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }
    
    private Transaction loadTransaction(Transaction transaction) {
        transaction = findOne(transaction.getId());

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        return transaction;
    }
    
    private Token loadtoken(Transaction transaction) {
        Token token = service.getTokens(transaction.getMerchant()).findOne(transaction.getToken());
        if (token == null) {
            throw new IllegalArgumentException("token not found");
        }
        return token;
    }
}
