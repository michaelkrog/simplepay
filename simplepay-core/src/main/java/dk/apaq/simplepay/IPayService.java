package dk.apaq.simplepay;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.data.ITokenCrud;
import dk.apaq.simplepay.data.ITransactionCrud;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;

/**
 * Service interface for Payments.
 */
public interface IPayService {

    /**
     * Gets the private user for the given merchant. Creates it if it does not yet exist.
     * @param merchant The merchant
     * @return The private system user.
     */
    SystemUser getOrCreatePrivateUser(Merchant merchant);
    
    /**
     * Gets the public user for the given merchant. Creates it if it does not yet exist.
     * @param merchant The merchant
     * @return The public system user.
     */
    SystemUser getOrCreatePublicUser(Merchant merchant);
    
    /**
     * Gets the user that is currently logged in.
     * @return The user or null if not user registered as logged in.
     */
    SystemUser getCurrentUser();
    
    /**
     * Retrieves the name of the current user. If not user is currenctly logged in 'Anonymous' is returned;
     * @return The name.
     */
    String getCurrentUsername();
    
    /**
     * Retrieves the user that has the specified username.
     * @param username The username
     * @return The user
     */
    SystemUser getUser(String username);
    
    /**
     * Retrieves a Repository for users.
     * @return The repository.
     */
    Repository<SystemUser, String> getUsers();
    
    /**
     * Retrieves a Repository for merchants.
     * @return The repository.
     */
    Repository<Merchant, String> getMerchants();

    /**
     * Reteives transaction by ordernumber
     * @param m The MErchant.
     * @param orderNumber The ordernumber
     * @return The Transaction or null if nothing matches.
     */
    Transaction getTransactionByRefId(Merchant m, String orderNumber);
    
    /**
     * Retrieves Repository for Transactions only for the given merchant..
     * @param merchant The Merchant.
     * @return The Repository.
     */
    ITransactionCrud getTransactions(Merchant merchant);
    
    /**
     * Retrieves Repository for Tokens only for the given MErchant.
     * @param merchant The merchant.
     * @return The Repository.
     */
    ITokenCrud getTokens(Merchant merchant);

    /**
     * Retrieves Repository for Events only for the given merchant and Event type.
     * @param <T> The type of event.
     * @param merchant The MErchant.
     * @param type The type of event.
     * @return The Repository of events.
     */
    <T extends Event> Repository<T, String> getEvents(Merchant merchant, Class<T> type);
}
