package dk.apaq.simplepay.model;

import javax.persistence.Entity;

/**
 *
 * @author krog
 */
@Entity
public class AcquirerAccess extends BaseEntity {
    private String acquirerRefId;
    private String acquirerApiKey;
    private Acquirer acquirer;
    
    /*
  rules:{
    instrumentBrand:["Visa",      //Specific rules for when to use this acquirer
                     "Dankort"], 
    maxAmount:100,
    minAmount:20
  },*/
}
