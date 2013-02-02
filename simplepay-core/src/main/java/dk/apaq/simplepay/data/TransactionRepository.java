package dk.apaq.simplepay.data;

import javax.persistence.EntityManager;

import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.*;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.apache.commons.lang.Validate;
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
    public Transaction createNew(Merchant merchant, String tokenId, String refId, Money money) {
        Validate.notNull(merchant, "merchant is null.");
        Validate.notNull(tokenId, "token is null.");
        
        Token token = service.getTokens(merchant).findOne(tokenId);
        Validate.notNull(token, "token given but could not be found in database.");
        
        //Get preferred gateway
        PaymentGatewayAccess access = token.getMerchant().getPaymentGatewayAccessPreferred(token.getData(), money);
        if (access == null) {
            throw new PaymentException("Unable to retrieve preferred payment type for merchant. [Merchant=" + token.getMerchant().getId() + "]");
        }

        Transaction transaction = new Transaction(token.getId(), refId, money, access.getPaymentGatewayType());


        //Create gateway
        IPaymentGateway gateway = gatewayManager.getPaymentGateway(access.getPaymentGatewayType());

        //authorize payment
        gateway.authorize(token.getMerchant(), access, token.getData(), money, refId, "", token.getPurpose());

        //Store authorized transaction
        transaction = save(transaction);

        //if token only for single usage then mark it expired
        if (token.getPurpose() == ETokenPurpose.SinglePayment) {
            token = service.getTokens(token.getMerchant()).markExpired(token.getId());
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
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());
        
        transaction.setAmountCharged(amount);
        transaction.setStatus(ETransactionStatus.Charged);

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.capture(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId(), amount);

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
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());
        
        transaction.setStatus(ETransactionStatus.Cancelled);

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.cancel(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId());

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
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());
        
        transaction.setAmountRefunded(amount);
        transaction.setStatus(ETransactionStatus.Refunded);

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.refund(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId(), amount);

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
    
    private PaymentGatewayAccess getGatewayAccess(Merchant merchant, EPaymentGateway type) {
        PaymentGatewayAccess access = merchant.getPaymentGatewayAccessByType(type);
        if (access == null) {
            throw new PaymentException("Unable to retrieve transactions payment type for merchant. [Merchant=" + merchant.getId() + 
                    ";PaymentType"+type+"]");
        }
        return access;
    }
}
