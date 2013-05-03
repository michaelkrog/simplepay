package dk.apaq.simplepay;

import dk.apaq.simplepay.data.service.IEventService;
import dk.apaq.simplepay.data.service.IMerchantService;
import dk.apaq.simplepay.data.service.ITokenService;
import dk.apaq.simplepay.data.service.ITransactionService;
import dk.apaq.simplepay.data.service.IUserService;
import dk.apaq.simplepay.model.Merchant;

/**
 * Service interface for Payments.
 */
public interface IPayService {

    
    /**
     * Retrieves a Repository for users.
     * @return The repository.
     */
    IUserService getUserService();
    
    /**
     * Retrieves a Repository for merchants.
     * @return The repository.
     */
    IMerchantService getMerchantService();

    /**
     * Retrieves Repository for Transactions only for the given merchant..
     * @param merchant The Merchant.
     * @return The Repository.
     */
    ITransactionService getTransactionService();
    
    /**
     * Retrieves Repository for Tokens only for the given MErchant.
     * @param merchant The merchant.
     * @return The Repository.
     */
    ITokenService getTokenService();

    /**
     * Retrieves Repository for Events only for the given merchant and Event type.
     * @param <T> The type of event.
     * @param merchant The MErchant.
     * @param type The type of event.
     * @return The Repository of events.
     */
    IEventService getEventService();
}
