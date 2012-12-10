package dk.apaq.simplepay.data;

import java.util.Date;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.repository.BaseRepositoryListener;
import dk.apaq.framework.repository.RepositoryEvent.List;
import dk.apaq.framework.repository.RepositoryEvent.WithEntity;
import dk.apaq.framework.repository.RepositoryEvent.WithIdAndEntity;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author krog
 */
public class DataAccess {

    public static class GeneralSecurity<BT> extends BaseRepositoryListener<BT, String> {

        private final Merchant owner;

        public GeneralSecurity(Merchant owner) {
            this.owner = owner;
        }

        @Override
        public void onBeforeList(List<BT, String> event) {
            Rule merchantRule = Rules.equals("merchant", owner);
            Criteria c = event.getCriteria();
            if (c == null) {
                c = new Criteria();
            }

            if (c != null && c.getRule() != null) {
                event.setCriteria(new Criteria(Rules.and(merchantRule, c.getRule()), c.getSorter(), c.getLimit()));
            } else {
                event.setCriteria(new Criteria(merchantRule, c.getSorter(), c.getLimit()));
            }
        }
    }

    public static class MerchantSecurity extends BaseRepositoryListener<Merchant, String> {

        private IPayService service;

        public MerchantSecurity(PayService service) {
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<Merchant, String> event) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            SystemUser user = service.getUser(username);
            Merchant usersMerchant = user.getMerchant();
            if (!usersMerchant.getId().equals(event.getEntity().getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        }
    }

    public static class TransactionSecurity extends GeneralSecurity<Transaction> {

        private final IPayService service;
        private final Merchant owner;

        public TransactionSecurity(PayService service, Merchant owner) {
            super(owner);
            if (owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<Transaction, String> event) {
            Transaction stale = service.getTransactions(owner).findOne(event.getEntityId());
            if (stale != null && !stale.getId().equals(event.getEntityId())) {
                throw new IllegalArgumentException("Ordernumber already used.");
            }

            stale = event.getRepository().findOne(event.getEntityId());
            if (stale.getMerchant() != null && stale.getMerchant().getId() != null) {
                if (!stale.getMerchant().getId().equals(event.getEntity().getMerchant().getId())) {
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
        public void onBeforeEntityCreate(WithEntity<Transaction, String> event) {
            if (service.getTransactionByRefId(owner, event.getEntity().getRefId()) != null) {
                throw new SecurityException("Ordernumber already used. [Merchant=" + owner.getId() + ";orderNumber=" + event.getEntity().getRefId() + "]");
            }

            checkStatus(event.getEntity());
            checkToken(event.getEntity());

            event.getEntity().setMerchant(owner);
            event.getEntity().setDateChanged(new Date());
        }

        private void checkToken(Transaction t) {
            String token = t.getToken();

            if (token == null) {
                throw new IllegalArgumentException("A transactation must have token specified.");
            }

            Token existingToken = service.getTokens(owner).findOne(token);

            if (t.getId() == null) { //new transaction
                if (existingToken.isExpired()) {
                    throw new SecurityException("Cannot use a token that has already been used.");
                }

                //token.set(true);
            } else {
                Transaction existingTransaction = service.getTransactions(owner).findOne(t.getId());
                if (!existingTransaction.getToken().equals(token)) {
                    throw new SecurityException("Cannot change token on transaction.");
                }
            }

        }

        private void checkStatus(Transaction t) {
            if (t.getId() == null) { //New transaction
                if (t.getStatus() != ETransactionStatus.Authorized) {
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

    public static class EventSecurity extends GeneralSecurity<Event> {

        private final IPayService service;
        private final Merchant owner;

        public EventSecurity(PayService service, Merchant owner) {
            super(owner);
            if (owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<Event, String> event) {
            throw new SecurityException("Events cannot be changed.");

        }

        @Override
        public void onBeforeEntityCreate(WithEntity<Event, String> event) {
            event.getEntity().setMerchant(owner);
        }
    }

    public static class TokenSecurity extends GeneralSecurity<Token> {

        private final IPayService service;
        private final Merchant owner;

        public TokenSecurity(PayService service, Merchant owner) {
            super(owner);
            if (owner.getId() == null) {
                throw new IllegalArgumentException("Merchant has never been persisted.");
            }
            this.owner = owner;
            this.service = service;
        }

        @Override
        public void onBeforeEntityUpdate(WithIdAndEntity<Token, String> event) {
            if (!event.getEntity().getMerchant().getId().equals(owner.getId())) {
                throw new SecurityException("Unable to change owner of token.");
            }

            Token existingToken = service.getTokens(owner).findOne(event.getEntityId());
            if (existingToken == null) {
                throw new IllegalArgumentException("No token for merchant with specified entityid[id=" + event.getEntityId() + "]");
            }

            /*if(existingToken.isUsed()) {
             throw new SecurityException("Cannot change token that has been used.");
             }*/
        }

        @Override
        public void onBeforeEntityCreate(WithEntity<Token, String> token) {
            token.getEntity().setMerchant(owner);
        }
    }
}
