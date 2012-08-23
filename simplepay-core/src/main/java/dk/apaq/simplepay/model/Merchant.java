package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.EPaymentGateway;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
    private String road;
    private String zipcode;
    private String city;
    private String country;
    
    @OneToMany(cascade= CascadeType.ALL)
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EPaymentGateway getPreferredPaymentGateway(Card card, Money money) {
        for(PaymentGatewayAccess pga : paymentGatewayAccesses) {
            //TODO Create som clever rule handling
            return pga.getPaymentGatewayType();
        }
        return null;
    }
    
}
