package dk.apaq.simplepay.common;

/**
 *
 * @author krog
 */
public enum EPaymentIntrument {
    //CHECKSTYLE:OFF
    American_Express(EPaymentType.CreditCard), 
    Dankort(EPaymentType.CreditCard), 
    Diners(EPaymentType.CreditCard), 
    Jcb(EPaymentType.CreditCard), 
    Mastercard(EPaymentType.CreditCard), 
    Visa(EPaymentType.CreditCard), 
    Visa_Electron(EPaymentType.CreditCard), 
    Money(EPaymentType.Cash), 
    Unknown(EPaymentType.Unknown);
    //CHECKSTYLE:ON

    private EPaymentType instrument;

    private EPaymentIntrument(EPaymentType instrument) {
        this.instrument = instrument;
    }

    /**
     * Retrieves the type of payment this instrument is.
     * @return The type of payment.
     */
    public EPaymentType getType() {
        return instrument;
    }
    
}
