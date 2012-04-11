package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;

/**
 *
 * @author krog
 */
public class Merchant {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String road;
    private String zipcode;
    private String city;
    private String country;
    private String publicKey;
    private String secretKey;
    private String username;
    private String password;
    
    private String gatewayUserId;
    private String gatewaySecret;
    private PaymentGatewayType gatewayType;

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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGatewayUserId() {
        return gatewayUserId;
    }

    public void setGatewayUserId(String merchantId) {
        this.gatewayUserId = merchantId;
    }

    public String getGatewaySecret() {
        return gatewaySecret;
    }

    public void setGatewaySecret(String merchantSecret) {
        this.gatewaySecret = merchantSecret;
    }

    public PaymentGatewayType getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(PaymentGatewayType gatewayType) {
        this.gatewayType = gatewayType;
    }
    
}
