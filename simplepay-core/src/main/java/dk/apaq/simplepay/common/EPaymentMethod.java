package dk.apaq.simplepay.common;

/**
 *
 * @author krog
 */
public enum EPaymentMethod {
    American_Express(EPaymentInstrument.CreditCard), 
    Dankort(EPaymentInstrument.CreditCard), 
    Diners(EPaymentInstrument.CreditCard), 
    Jcb(EPaymentInstrument.CreditCard), 
    Mastercard(EPaymentInstrument.CreditCard), 
    Visa(EPaymentInstrument.CreditCard), 
    Visa_Electron(EPaymentInstrument.CreditCard), 
    Money(EPaymentInstrument.Cash);

    private EPaymentInstrument instrument;

    private EPaymentMethod(EPaymentInstrument instrument) {
        this.instrument = instrument;
    }

    public EPaymentInstrument getInstrument() {
        return instrument;
    }
    
}
