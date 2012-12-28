package dk.apaq.simplepay;

import dk.apaq.simplepay.common.EPaymentIntrument;
import dk.apaq.simplepay.model.Card;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for handling card information. <br>
 * Utilizes generating card with encrypted information, resolved card type etc.
 */
public class CardService {

    private final StringEncryptor encryptor;

    public CardService(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }
    
    public Card generateCard(String number, int expireMonth, int expireYear, String cvd) {
        return generateCard(null, number, expireMonth, expireYear, cvd);
    }
    
    public Card generateCard(String name, String number, int expireMonth, int expireYear, String cvd) {
        boolean valid = isValid(number);
        String last4 = getLast4(number);
        String encryptedNumber = encryptor.encrypt(number);
        String encryptedCvd = encryptor.encrypt(cvd);
        EPaymentIntrument type = getType(number);
        return new Card(name, encryptedNumber, last4, expireMonth, expireYear, encryptedCvd, valid, type);
    }
    
    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }
    
    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
    
    public boolean isValid(String number) {
        if (number == null) {
            return false;
        }

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

    public String getLast4(String number) {
        if (number == null) {
            return null;
        } else {
            int noOfChars = Math.min(number.length(), 4);
            return number.substring(number.length() - noOfChars, number.length());
        }
    }

    public EPaymentIntrument getType(String number) {
        if (number == null || !isValid(number)) {
            return EPaymentIntrument.Unknown;
        }

        if (isDankort(number)) {
            return EPaymentIntrument.Dankort;
        }

        if (isVisaElectron(number)) {
            return EPaymentIntrument.Visa_Electron;
        }

        if (isVisa(number)) {
            return EPaymentIntrument.Visa;
        }

        if (isAmericanExpress(number)) {
            return EPaymentIntrument.American_Express;
        }

        if (isDiners(number)) {
            return EPaymentIntrument.Diners;
        }

        if (isJcb(number)) {
            return EPaymentIntrument.Jcb;
        }

        if (isMasterCard(number)) {
            return EPaymentIntrument.Mastercard;
        }

        return EPaymentIntrument.Unknown;
    }

    private boolean isAmericanExpress(String number) {
        return number.startsWith("34") || number.startsWith("37");
    }

    private boolean isDankort(String number) {
        return number.startsWith("5019") || number.startsWith("4571");
    }

    private boolean isDiners(String number) {
        boolean value = number.startsWith("36")
                || number.startsWith("54")
                || number.startsWith("55")
                || number.startsWith("2014")
                || number.startsWith("2149");

        if (value != true && number.length() > 2) {
            int first3 = Integer.parseInt(number.substring(0, 2));
            value = first3 >= 300 && first3 <= 305;
        }
        return value;
    }

    private boolean isJcb(String number) {
        boolean value = false;

        if (number.length() > 3) {
            int first4 = Integer.parseInt(number.substring(0, 3));
            value = first4 >= 3528 && first4 <= 3589;
        }
        return value;
    }

    private boolean isMasterCard(String number) {
        boolean value = false;

        if (number.length() > 1) {
            int first2 = Integer.parseInt(number.substring(0, 2));
            value = first2 >= 51 && first2 <= 55;
        }
        return value;
    }

    private boolean isVisa(String number) {
        return number.startsWith("4");
    }

    private boolean isVisaElectron(String number) {
        boolean value = number.startsWith("4026")
                || number.startsWith("417500")
                || number.startsWith("4508")
                || number.startsWith("4844")
                || number.startsWith("4913")
                || number.startsWith("4917");

        if (value != true && number.length() > 2) {
            int first3 = Integer.parseInt(number.substring(0, 2));
            value = first3 >= 300 && first3 <= 305;
        }
        return value;
    }
}
