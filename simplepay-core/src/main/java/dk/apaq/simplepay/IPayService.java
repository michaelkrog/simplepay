package dk.apaq.simplepay;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public interface IPayService {

    SystemUser getCurrentUser();

    Crud.Complete<String, Merchant> getMerchants();

    @Transactional
    SystemUser getOrCreatePrivateUser(Merchant merchant);

    @Transactional
    SystemUser getOrCreatePublicUser(Merchant merchant);

    Transaction getTransactionByOrderNumber(Merchant m, String orderNumber);

    Crud.Complete<String, Transaction> getTransactions(Merchant merchant);

    SystemUser getUser(String username);

    Crud.Complete<String, SystemUser> getUsers();

    void setApplicationContext(ApplicationContext ac) throws BeansException;
    
}
