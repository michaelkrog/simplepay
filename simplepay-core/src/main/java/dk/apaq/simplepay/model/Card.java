package dk.apaq.simplepay.model;

/**
 *
 * @author krog
 */
public class Card implements PaymentInstrument {
    
    private String number;
    private int expMonth;
    private int expYear;
    private String name;
    private String cvd;
    private String country;
    
    //private String type; f.x Visa

    
    public String getCountry() {
        return country;
    }

    public String getCvd() {
        return cvd;
    }

    public void setCvd(String cvd) {
        this.cvd = cvd;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public String getLast4() {
        if(number == null) {
            return null;
        } else {
            int noOfChars = Math.min(number.length(), 4);
            return number.substring(number.length() - noOfChars, number.length());
        }
    }
    
    public boolean isValid() {
        //TODO Wether it is valid or not.
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
