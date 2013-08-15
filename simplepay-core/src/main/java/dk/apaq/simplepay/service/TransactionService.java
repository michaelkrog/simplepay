
package dk.apaq.simplepay.service;

import java.io.Serializable;
import java.util.Date;

import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.repository.ITokenRepository;
import dk.apaq.simplepay.repository.ITransactionRepository;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.apache.commons.lang.Validate;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Javadoc
 */
public class TransactionService extends BaseService<Transaction, ITransactionRepository> {

    private UserService userService;
    private TokenService tokenService;
    private PaymentGatewayManager gatewayManager;

    @Autowired
    public void setGatewayManager(PaymentGatewayManager gatewayManager) {
        this.gatewayManager = gatewayManager;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    
    public Transaction createNew(String tokenId, String refId, Money money) {
        Validate.notNull(tokenId, "token is null.");
        Merchant merchant = userService.getCurrentUser(true).getMerchant();
        
        Token token = tokenService.findOne(tokenId);
        Validate.notNull(token, "Token given but could not be found in database.");
        Validate.isTrue(!token.isExpired(), "Token is expired.");

        if (repository.findByMerchantAndRefId(merchant, refId) != null) {
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
        transaction = repository.save(transaction);

        //if token only for single usage then mark it expired
        if (token.getPurpose() == ETokenPurpose.SinglePayment) {
            token = tokenService.markExpired(token.getId());
        }

        TransactionEvent evt = new TransactionEvent(transaction, userService.getCurrentUsername(), ETransactionStatus.Authorized,
                RequestInformationHelper.getRemoteAddress());
        //service.getEvents(token.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    public Transaction charge(Transaction transaction, long amount) {
        Merchant merchant = userService.getCurrentUser().getMerchant();
        transaction = loadTransaction(merchant, transaction);
        Token token = tokenService.findOne(transaction.getToken());
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());

        transaction.setAmountCharged(amount);
        transaction.setStatus(ETransactionStatus.Charged);
        transaction.setDateChanged(new Date());

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.capture(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId(), amount);

        transaction = repository.save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, userService.getCurrentUsername(), ETransactionStatus.Charged,
                RequestInformationHelper.getRemoteAddress());
        //service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    public Transaction cancel(Transaction transaction) {
        Merchant merchant = userService.getCurrentUser().getMerchant();
        transaction = loadTransaction(merchant, transaction);
        Token token = tokenService.findOne(transaction.getToken());
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());

        transaction.setStatus(ETransactionStatus.Cancelled);
        transaction.setDateChanged(new Date());

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.cancel(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId());

        transaction = repository.save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, userService.getCurrentUsername(), ETransactionStatus.Cancelled,
                RequestInformationHelper.getRemoteAddress());
        //service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    public Transaction refund(Transaction transaction, long amount) {
        Merchant merchant = userService.getCurrentUser().getMerchant();
        transaction = loadTransaction(merchant, transaction);
        Token token = tokenService.findOne(transaction.getToken());
        PaymentGatewayAccess access = getGatewayAccess(token.getMerchant(), transaction.getGatewayType());

        transaction.setAmountRefunded(amount);
        transaction.setStatus(ETransactionStatus.Refunded);
        transaction.setDateChanged(new Date());

        IPaymentGateway gateway = gatewayManager.getPaymentGateway(transaction.getGatewayType());
        gateway.refund(token.getMerchant(), access, transaction.getGatewayTransactionId(), transaction.getRefId(), amount);

        transaction = repository.save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, userService.getCurrentUsername(), ETransactionStatus.Refunded,
                RequestInformationHelper.getRemoteAddress());
        //service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }
    
    private Transaction loadTransaction(Merchant merchant, Transaction transaction) {
        transaction = repository.findByMerchantAndId(merchant, transaction.getId());

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        
        return transaction;
    }

    private PaymentGatewayAccess getGatewayAccess(Merchant merchant, EPaymentGateway type) {
        PaymentGatewayAccess access = merchant.getPaymentGatewayAccessByType(type);
        if (access == null) {
            throw new PaymentException("Unable to retrieve transactions payment type for merchant. [Merchant=" + merchant.getId()
                    + ";PaymentType" + type + "]");
        }
        return access;
    }

    public Transaction getTransactionByRefId(String refId) {
        Merchant merchant = userService.getCurrentUser().getMerchant();
        return repository.findByMerchantAndRefId(merchant, refId);
    }
    
    
}
