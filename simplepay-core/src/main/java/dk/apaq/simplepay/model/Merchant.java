package dk.apaq.simplepay.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import org.apache.commons.lang.Validate;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
@Entity
public class Merchant extends BaseEntity {

    private String name;
    private String email;
    private String phone;
    private String street;
    private String postalCode;
    private String city;
    private String countryCode;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PaymentGatewayAccess> paymentGatewayAccesses = new ArrayList<PaymentGatewayAccess>();

    public List<PaymentGatewayAccess> getPaymentGatewayAccesses() {
        return paymentGatewayAccesses;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Retrieves the 3-letter countryCode.
     * @return The countryCode or null if not set.
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the 3-letter countryCode.
     * @param country The countrycode fx. DNK
     */
    public void setCountry(String country) {
        this.countryCode = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PaymentGatewayAccess getPaymentGatewayAccessPreferred(Card card, Money money) {
        Validate.notNull(card, "The card specified is null.");
        Validate.notNull(money, "The money specified is null.");
        for (PaymentGatewayAccess pga : paymentGatewayAccesses) {
            if(pga.getSpecificValidInstruments().isEmpty() || pga.getSpecificValidInstruments().contains(card.getResolvedInstrument())) {
                return pga;
            }
        }
        return null;
    }
    
    public PaymentGatewayAccess getPaymentGatewayAccessByType(EPaymentGateway type) {
        for (PaymentGatewayAccess pga : paymentGatewayAccesses) {
            if(pga.getPaymentGatewayType() == type) {
                return pga;
            }
        }
        return null;
    }
    
    
}

