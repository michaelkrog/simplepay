package dk.apaq.simplepay;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.crud.ITokenCrud;
import dk.apaq.simplepay.crud.ITransactionCrud;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;

/**
 *
 * @author michael
 */
public interface IPayService {

    
    SystemUser getOrCreatePrivateUser(Merchant merchant);
    SystemUser getOrCreatePublicUser(Merchant merchant);
    SystemUser getCurrentUser();
    SystemUser getUser(String username);
    Crud.Complete<String, SystemUser> getUsers();

    
    Crud.Complete<String, Merchant> getMerchants();

    Transaction getTransactionByOrderNumber(Merchant m, String orderNumber);
    
    ITransactionCrud getTransactions(Merchant merchant);
    ITokenCrud getTokens(Merchant merchant);

    <T extends Event> Crud.Complete<String, T> getEvents(Merchant merchant, Class<T> type);
}
