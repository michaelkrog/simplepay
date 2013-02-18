package dk.apaq.simplepay.data;


import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.simplepay.model.Merchant;
import org.apache.commons.lang.Validate;

/**
 * Defines logic for access to data. This is used by the repositories returned by the service interface.
 */
public class DataAccess {

    public static void checkMerchant(Merchant merchant) {
        Validate.notNull(merchant, "merchant is null.");
        if (merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant has never been persisted.");
        }
    }

    public static Criteria appendMerchantCriteria(Criteria c, Merchant merchant) {
        Rule merchantRule = Rules.equals("merchant", merchant);
        if (c == null) {
            c = new Criteria();
        }

        if (c != null && c.getRule() != null) {
            c = new Criteria(Rules.and(merchantRule, c.getRule()), c.getSorter(), c.getLimit());
        } else {
            c = new Criteria(merchantRule, c.getSorter(), c.getLimit());
        }
        return c;
    }

    
    
}
