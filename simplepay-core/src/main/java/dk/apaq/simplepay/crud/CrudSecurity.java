package dk.apaq.simplepay.crud;

import dk.apaq.crud.CrudEvent.List;
import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithIdAndEntity;
import dk.apaq.crud.core.BaseCrudListener;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.util.Date;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author krog
 */
public class CrudSecurity {
    
    public static class MerchantSecurity extends BaseCrudListener<String, Merchant> {

        private IPayService service;

        public MerchantSecurity(PayService service) {
            this.service = service;
        }
        
        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Merchant> event) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            SystemUser user = service.getUser(username); 
            Merchant usersMerchant = user.getMerchant();
            if(!usersMerchant.getId().equals(event.getEntity().getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        }
        
    }
    
    public static class TransactionSecurity extends BaseCrudListener<String, Transaction> {
        private final IPayService service;
        private final Merchant owner;

        
        public TransactionSecurity(PayService service, Merchant owner) {
            if(owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Transaction> event) {
            Transaction stale = service.getTransactions(owner).read(event.getEntityId());
            if(stale != null && !stale.getId().equals(event.getEntityId())) {
                throw new IllegalArgumentException("Ordernumber already used.");
            }
            
            stale = event.getCrud().read(event.getEntityId());
            if(stale.getMerchant()!=null && stale.getMerchant().getId() != null) {
              if(!stale.getMerchant().getId().equals(event.getEntity().getMerchant().getId())) {
                  throw new SecurityException("Not allowed to take other merchants transactions.");
              }
            } else {
                event.getEntity().setMerchant(owner);
            }
            
            checkStatus(event.getEntity());
            checkToken(event.getEntity());
            
            event.getEntity().setDateChanged(new Date());
            
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<String, Transaction> event) {
            if(service.getTransactionByRefId(owner, event.getEntity().getRefId()) != null) {
                throw new SecurityException("Ordernumber already used. [Merchant="+owner.getId()+";orderNumber="+event.getEntity().getRefId()+"]");
            }
            
            checkStatus(event.getEntity());
            checkToken(event.getEntity());
            
            event.getEntity().setMerchant(owner);
            event.getEntity().setDateChanged(new Date());
        }

        @Override
        public void onBeforeList(List<String, Transaction> event) {
            Filter merchantFilter = new CompareFilter("merchant", owner, CompareFilter.CompareType.Equals);
            if(event.getListSpecification().getFilter() != null) {
                event.getListSpecification().setFilter(new AndFilter(merchantFilter, event.getListSpecification().getFilter()));
            } else {
                event.getListSpecification().setFilter(merchantFilter);
            }
        }
        
        private void checkToken(Transaction t) {
            String token = t.getToken();
            
            if(token == null) {
                throw new IllegalArgumentException("A transactation must have token specified.");
            }
            
            Token existingToken = service.getTokens(owner).read(token);
            
            if(t.getId() == null) { //new transaction
                if(existingToken.isExpired()) {
                    throw new SecurityException("Cannot use a token that has already been used.");
                }
                
                //token.set(true);
            } else {
               Transaction existingTransaction = service.getTransactions(owner).read(t.getId());
               if(!existingTransaction.getToken().equals(token)) {
                   throw new SecurityException("Cannot change token on transaction.");
               } 
            }
            
        }
        
        private void checkStatus(Transaction t) {
            if(t.getId() == null) { //New transaction
                if(t.getStatus() != ETransactionStatus.Authorized) {
                    throw new SecurityException("A new transaction status can only be persisted with status Ready.");
                }
            } else {
                //These are valid flows:
                //  Ready -> Cancelled
                //  Ready -> Charged -> Refunded
                
                //TODO We cannot check the flow because we dont detach hibernate objects.
                // Therefore when we read a transaction, changes its status and updates it, then the object we read for comparison
                // is the same object as we have changed. Need to figure out how to test this.
                
                /*
                Transaction existingTransaction = service.getTransactions(owner).read(t.getId());
                if(t.getStatus() == TransactionStatus.Cancelled && existingTransaction.getStatus() != TransactionStatus.Ready) {
                    throw new SecurityException("Only transactions in status Ready can be cancelled.");
                }
                
                if(t.getStatus() == TransactionStatus.Charged && existingTransaction.getStatus() != TransactionStatus.Ready) {
                    throw new SecurityException("Only transactions in status Ready can be charged.");
                }
                
                if(t.getStatus() == TransactionStatus.Refunded && existingTransaction.getStatus() != TransactionStatus.Charged) {
                    throw new SecurityException("Only transactions in status Charged can be refunded.");
                }*/
            }
        }
        
        
        
    }
    
    public static class EventSecurity extends BaseCrudListener<String, Event> {
        private final IPayService service;
        private final Merchant owner;

        
        public EventSecurity(PayService service, Merchant owner) {
            if(owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Event> event) {
            throw new SecurityException("Events cannot be changed.");
            
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<String, Event> event) {
            event.getEntity().setMerchant(owner);
        }

        @Override
        public void onBeforeList(List<String, Event> event) {
            Filter merchantFilter = new CompareFilter("merchant", owner, CompareFilter.CompareType.Equals);
            if(event.getListSpecification().getFilter() != null) {
                event.getListSpecification().setFilter(new AndFilter(merchantFilter, event.getListSpecification().getFilter()));
            } else {
                event.getListSpecification().setFilter(merchantFilter);
            }
        }
    }
    
    public static class TokenSecurity extends BaseCrudListener<String, Token> {
        private final IPayService service;
        private final Merchant owner;

        
        public TokenSecurity(PayService service, Merchant owner) {
            if(owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<String, Token> event) {
            if(!event.getEntity().getMerchant().getId().equals(owner.getId())) {
                throw new SecurityException("Unable to change owner of token.");
            }
            
            Token existingToken = service.getTokens(owner).read(event.getEntityId());
            if(existingToken == null) {
                throw new IllegalArgumentException("No token for merchant with specified entityid[id="+event.getEntityId()+"]");
            }
            
            /*if(existingToken.isUsed()) {
                throw new SecurityException("Cannot change token that has been used.");
            }*/
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<String, Token> token) {
            token.getEntity().setMerchant(owner);
        }

        @Override
        public void onBeforeList(List<String, Token> event) {
            Filter merchantFilter = new CompareFilter("merchant", owner, CompareFilter.CompareType.Equals);
            if(event.getListSpecification().getFilter() != null) {
                event.getListSpecification().setFilter(new AndFilter(merchantFilter, event.getListSpecification().getFilter()));
            } else {
                event.getListSpecification().setFilter(merchantFilter);
            }
        }
    }
}
