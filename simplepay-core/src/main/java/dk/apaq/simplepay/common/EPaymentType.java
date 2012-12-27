package dk.apaq.simplepay.common;

/**
 * Enum for Payment Types.<br>
 * <br>
 * A payment type never consists of a brand name. CreditCard and Cash are payment types wheras VISA and MasterCard are not.
 */
public enum EPaymentType {
    //CHECKSTYLE:OFF
    CreditCard, Cash, Unknown;
    //CHECKSTYLE:ON
}
