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
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
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
            Transaction stale = service.getTransactionByOrderNumber(owner, event.getEntity().getOrderNumber());
            if(stale != null && !stale.getId().equals(event.getEntityId())) {
                throw new IllegalArgumentException("Ordernumber already used.");
            }
            
            stale = event.getCrud().read(event.getEntityId());
            if(stale.getMerchant()!=null && stale.getMerchant().getId() != null) {
              if(!stale.getMerchant().getId().equals(event.getEntity().getMerchant().getId())) {
                  throw new SecurityException("Not allowed to take other merchants transcations.");
              }
            } else {
                event.getEntity().setMerchant(owner);
            }
            
            //TODO: Check for legal transaction status
            
            event.getEntity().setDateChanged(new Date());
            
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<String, Transaction> event) {
            if(service.getTransactionByOrderNumber(owner, event.getEntity().getOrderNumber()) != null) {
                throw new IllegalArgumentException("Ordernumber already used.");
            }
            
            //TODO: Check for legal transaction status
            
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
}
