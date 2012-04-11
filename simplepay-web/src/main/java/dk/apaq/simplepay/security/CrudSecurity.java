package dk.apaq.simplepay.security;

import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;

/**
 *
 * @author krog
 */
public class CrudSecurity {
    
    public static class MerchantSecurity extends BaseCrudListener<String, Merchant> {
 
    }
    
    public static class TransactionSecurity extends BaseCrudListener<String, Transaction> {
        private final Merchant owner;

        public TransactionSecurity(Merchant owner) {
            this.owner = owner;
        }
        
        
    }
}
