package dk.apaq.simplepay.data;

import java.util.Date;
import java.util.List;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.Repository;
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
public class TransactionRepository extends RepositoryWrapper<Transaction, String> implements ITransactionRepository {

    private PaymentGatewayManager gatewayManager;
    private IPayService service;

    private Merchant merchant;
    
    /**
     * Constructs an instance of Transaction Repository.
     *
     * @param em The entitymanager that holds transactions.
     */
    public TransactionRepository(Repository<Transaction, String> repository, IPayService service, PaymentGatewayManager gatewayManager) {
        super(repository);
        this.service = service;
        this.gatewayManager = gatewayManager;
    }
    
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    

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

        Date now = new Date();
        Transaction transaction = new Transaction(token.getId(), refId, money, access.getPaymentGatewayType());
        transaction.setMerchant(merchant);
        transaction.setDateCreated(now);
        transaction.setDateChanged(now);

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
        transaction.setDateChanged(new Date());

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
        transaction.setDateChanged(new Date());

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
        transaction.setDateChanged(new Date());

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
    public Iterable<Transaction> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public Iterable<Transaction> findAll(Criteria criteria) {
        return super.findAll(DataAccess.appendMerchantCriteria(criteria, merchant));
    }
    
    private Transaction loadTransaction(Transaction transaction) {
        transaction = findOne(transaction.getId());

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        
        if(transaction.getMerchant() == null) {
            throw new IllegalStateException("Transaction found but is an orphan and cannot be correctly retrieved.");
        }
        
        if(!transaction.getMerchant().getId().equals(merchant.getId())) {
            throw new SecurityException("Merchant not allowed to access the transaction as it is owned by another merchant.");
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
