package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class Merchant implements Serializable {
    
    //private static final String DEFAULT_SECRET = "SECRET";
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String email;
    private String phone;
    private String road;
    private String zipcode;
    private String city;
    private String country;
    /*
    private String gatewayUserId;
    private String gatewaySecret = DEFAULT_SECRET;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentGatewayType gatewayType = PaymentGatewayType.Test;
*/
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

  /*  public String getGatewayUserId() {
        return gatewayUserId;
    }

    public void setGatewayUserId(String merchantId) {
        this.gatewayUserId = merchantId;
    }

    public String getGatewaySecret() {
        return gatewaySecret;
    }

    public void setGatewaySecret(String merchantSecret) {
        if(merchantSecret == null) merchantSecret = DEFAULT_SECRET;
        this.gatewaySecret = merchantSecret;
    }

    public PaymentGatewayType getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(PaymentGatewayType gatewayType) {
        this.gatewayType = gatewayType;
    }
*/
    
}
