package dk.apaq.simplepay.model;

/**
 *
 * @author krog
 */
public class Card {
    
    private String number;
    private int expMonth;
    private int expYear;
    private String name;
    private String cvd;
    private String country;
    
    //private String type; f.x Visa

    public Card(String number, int expMonth, int expYear, String cvd) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvd = cvd;
    }

    public Card(String name, String number, int expMonth, int expYear, String cvd) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.name = name;
        this.cvd = cvd;
    }

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
        if(number == null) return false;
        
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
