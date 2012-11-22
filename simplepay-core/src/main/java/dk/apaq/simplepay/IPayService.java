package dk.apaq.simplepay;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.data.ITokenCrud;
import dk.apaq.simplepay.data.ITransactionCrud;
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
    /**
     * Retrieves the name of the current user. If not user is currenctly logged in 'Anonymous' is returned;
     * @return 
     */
    String getCurrentUsername();
    
    SystemUser getUser(String username);
    Repository<SystemUser, String> getUsers();
    
    Repository<Merchant, String> getMerchants();

    Transaction getTransactionByRefId(Merchant m, String orderNumber);
    
    ITransactionCrud getTransactions(Merchant merchant);
    ITokenCrud getTokens(Merchant merchant);

    <T extends Event> Repository<T, String> getEvents(Merchant merchant, Class<T> type);
}
