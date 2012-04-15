package dk.apaq.simplepay.crud;

import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithIdAndEntity;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.MerchantUserDetailsHolder;

/**
 *
 * @author krog
 */
public class CrudSecurity {
    
    public static class MerchantSecurity extends BaseCrudListener<String, Merchant> {

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Merchant> event) {
            Merchant principal = MerchantUserDetailsHolder.getMerchantUserDetails().getMerchant();
            if(!principal.getId().equals(event.getEntity().getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        }
        
    }
    
    public static class TransactionSecurity extends BaseCrudListener<String, Transaction> {
        private final Merchant owner;

        public TransactionSecurity(Merchant owner) {
            if(owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Transaction> event) {
            Transaction stale = event.getCrud().read(event.getEntityId());
            if(stale.getMerchant()!=null && stale.getMerchant().getId() != null) {
              if(!stale.getMerchant().getId().equals(event.getEntity().getMerchant().getId())) {
                  throw new SecurityException("Not allowed to take other merchants transcations.");
              }
            } else {
                event.getEntity().setMerchant(owner);
            }
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<String, Transaction> event) {
            event.getEntity().setMerchant(owner);
        }
        
        
        
    }
}
