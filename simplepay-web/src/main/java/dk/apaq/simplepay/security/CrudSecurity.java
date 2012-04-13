package dk.apaq.simplepay.security;

import dk.apaq.crud.CrudEvent.WithIdAndEntity;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;

/**
 *
 * @author krog
 */
public class CrudSecurity {
    
    public static class MerchantSecurity extends BaseCrudListener<String, Merchant> {

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Merchant> event) {
            Merchant principal = MerchantUserDetailsHolder.getMerchantUserDetails().getMerchant();
            if(principal.getId().equals(event.getEntity().getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        }
        
    }
    
    public static class TransactionSecurity extends BaseCrudListener<String, Transaction> {
        private final Merchant owner;

        public TransactionSecurity(Merchant owner) {
            this.owner = owner;
        }
        
    }
}
