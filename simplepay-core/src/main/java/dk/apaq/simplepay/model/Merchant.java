package dk.apaq.simplepay.model;

import javax.persistence.Entity;

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
