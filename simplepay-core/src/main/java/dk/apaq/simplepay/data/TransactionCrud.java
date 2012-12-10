package dk.apaq.simplepay.data;

import javax.persistence.EntityManager;

import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IDirectPaymentGateway;
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
public class TransactionCrud extends EntityManagerRepositoryForSpring<Transaction, String> implements ITransactionCrud {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    private IPayService service;

    public TransactionCrud(EntityManager em) {
        super(em, Transaction.class);

    }

    @Transactional
    public Transaction createNew(Token token, String refId, Money money) {
        Transaction transaction = new Transaction(token.getId(), refId, money);

        //Get preferred gateway
        EPaymentGateway type = token.getMerchant().getPreferredPaymentGateway(token.getData(), money);
        if (type == null) {
            throw new PaymentException("Unable to retrieve preferred payment type for merchant. [Merchant=" + token.getMerchant().getId() + "]");
        }

        //Create gateway
        IPaymentGateway gateway = gatewayManager.createPaymentGateway(type);

        //authorize payment
        ((IDirectPaymentGateway) gateway).authorize(token, money, refId, "");

        //Store authorized transaction
        transaction = save(transaction);

        //if token only for single usage then mark it expired
        if (token.getPurpose() == ETokenPurpose.SinglePayment) {
            service.getTokens(token.getMerchant()).markExpired(token);
        }

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Authorized, RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    public Transaction charge(Transaction transaction, long amount) {
        transaction = findOne(transaction.getId());
        transaction.setAmountCharged(amount);
        transaction.setStatus(ETransactionStatus.Charged);

        IPaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.capture(transaction.getToken(), amount);

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Charged, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    public Transaction cancel(Transaction transaction) {
        transaction = findOne(transaction.getId());
        transaction.setStatus(ETransactionStatus.Cancelled);

        IPaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.cancel(transaction.getToken());

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Cancelled, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }

    @Transactional
    public Transaction refund(Transaction transaction, long amount) {
        transaction = findOne(transaction.getId());
        transaction.setAmountRefunded(amount);
        transaction.setStatus(ETransactionStatus.Refunded);

        IPaymentGateway gateway = null;//gatewayManager.createPaymentGateway(transaction.getToken().getGatewayType());
        //gateway.refund(transaction.getToken(), amount);

        transaction = save(transaction);

        TransactionEvent evt = new TransactionEvent(transaction, service.getCurrentUsername(), ETransactionStatus.Refunded, RequestInformationHelper.getRemoteAddress());
        service.getEvents(transaction.getMerchant(), TransactionEvent.class).save(evt);

        return transaction;
    }
}
