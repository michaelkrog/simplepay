package dk.apaq.simplepay.data;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.Repository;
import dk.apaq.framework.repository.jpa.EntityManagerRepository;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.*;
import dk.apaq.simplepay.util.IdGenerator;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.apache.commons.lang.Validate;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public class TransactionRepository extends EntityManagerRepository<Transaction, String> implements ITransactionRepository {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    private IPayService service;

    private Merchant merchant;
    
    /**
     * Constructs an instance of Transaction Repository.
     *
     * @param em The entitymanager that holds transactions.
     */
    public TransactionRepository(EntityManager em, Merchant merchant) {
        super(em, Transaction.class);
        this.merchant = merchant;
    }

    /*
     * @Override
     public void onBeforeEntitySave(WithIdAndEntity<Transaction, String> event) {
     Transaction stale = service.getTransactions(getOwner()).findOne(event.getEntityId());
     if (stale != null && !stale.getId().equals(event.getEntityId())) {
     throw new IllegalArgumentException("Ordernumber already used.");
     }

     if (stale != null && stale.getMerchant() != null && stale.getMerchant().getId() != null) {
     if (!stale.getMerchant().getId().equals(event.getEntity().getMerchant().getId())) {
     throw new SecurityException("Not allowed to take other merchants transactions.");
     }
     } else {
     event.getEntity().setMerchant(getOwner());
     }

     checkStatus(event.getEntity());
     checkToken(event.getRepository(), event.getEntity());

     event.getEntity().setDateChanged(new Date());

     }

     private void checkToken(Repository rep, Transaction t) {
     String token = t.getToken();

     if (token == null) {
     throw new IllegalArgumentException("A transactation must have token specified.");
     }

     Token existingToken = service.getTokens(getOwner()).findOne(token);

     if (t.getId() == null || rep.findOne(t.getId()) == null) { //new transaction
     if (existingToken.isExpired()) {
     throw new SecurityException("Cannot use a token that has already been used.");
     }

     //token.set(true);
     } else {
     Transaction existingTransaction = service.getTransactions(getOwner()).findOne(t.getId());
     if (!existingTransaction.getToken().equals(token)) {
     throw new SecurityException("Cannot change token on transaction.");
     }
     }

     }

     private void checkStatus(Transaction t) {
     if (t.getId() == null) { //New transaction
     if (t.getStatus() != ETransactionStatus.Authorized) {
     throw new SecurityException("A new transaction status can only be persisted with status Ready.");
     }
     } else {
     //These are valid flows:
     //  Ready -> Cancelled
     //  Ready -> Charged -> Refunded
     //TODO We cannot check the flow because we dont detach hibernate objects.
     // Therefore when we read a transaction, changes its status and updates it, then the object we read for comparison
     // is the same object as we have changed. Need to figure out how to test this.
     / *
     Transaction existingTransaction = service.getTransactions(owner).read(t.getId());
     if(t.getStatus() == TransactionStatus.Cancelled && existingTransaction.getStatus() != TransactionStatus.Ready) {
     throw new SecurityException("Only transactions in status Ready can be cancelled.");
     }
                
     if(t.getStatus() == TransactionStatus.Charged && existingTransaction.getStatus() != TransactionStatus.Ready) {
     throw new SecurityException("Only transactions in status Ready can be charged.");
     }
                
     if(t.getStatus() == TransactionStatus.Refunded && existingTransaction.getStatus() != TransactionStatus.Charged) {
     throw new SecurityException("Only transactions in status Charged can be refunded.");
     } * /
     }
     } 
     * 
     */
    @Transactional
    @Override
    public Transaction createNew(Merchant merchant, String tokenId, String refId, Money money) {
        Validate.notNull(merchant, "merchant is null.");
        Validate.notNull(tokenId, "token is null.");

        Token token = service.getTokens(merchant).findOne(tokenId);
        Validate.notNull(token, "Token given but could not be found in database.");
        Validate.isTrue(!token.isExpired(), "Token is expired.");

        if (service.getTransactionByRefId(merchant, refId) != null) {
            throw new SecurityException("Ordernumber already used. [Merchant=" + merchant.getId() + ";orderNumber=" + refId + "]");
        }

        //Get preferred gateway
        PaymentGatewayAccess access = token.getMerchant().getPaymentGatewayAccessPreferred(token.getData(), money);
        if (access == null) {
            throw new PaymentException("Unable to retrieve preferred payment type for merchant. [Merchant=" + token.getMerchant().getId() + "]");
        }

        Transaction transaction = new Transaction(token.getId(), refId, money, access.getPaymentGatewayType());
        transaction.setId(IdGenerator.generateUniqueId("p"));

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

    @Override
    public <S extends Transaction> List<S> save(Iterable<S> entities) {
        throw new UnsupportedOperationException("Use charge, cancel or refund.");
    }

    @Override
    public List<Transaction> findAll(Criteria criteria) {
        return super.findAll(DataAccess.appendMerchantCriteria(criteria, merchant));
    }
    
    private Transaction loadTransaction(Transaction transaction) {
        transaction = findOne(transaction.getId());

        if (transaction == null || !transaction.getMerchant().getId().equals( merchant.getId())) {
            throw new IllegalArgumentException("Transaction not found");
        }
        return transaction;
    }

    private Token loadtoken(Transaction transaction) {
        Token token = service.getTokens(transaction.getMerchant()).findOne(transaction.getToken());
        if (token == null || !token.getMerchant().getId().equals( merchant.getId())) {
            throw new IllegalArgumentException("token not found");
        }
        return token;
    }

    private PaymentGatewayAccess getGatewayAccess(Merchant merchant, EPaymentGateway type) {
        PaymentGatewayAccess access = merchant.getPaymentGatewayAccessByType(type);
        if (access == null) {
            throw new PaymentException("Unable to retrieve transactions payment type for merchant. [Merchant=" + merchant.getId()
                    + ";PaymentType" + type + "]");
        }
        return access;
    }
}
