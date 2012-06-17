package dk.apaq.simplepay.crud;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface ITokenCrud extends Crud.Filterable<String, Token> {
    
    Token createNew(PaymentGatewayType gatewayType, String orderNumber, String description);
    Token authorizedRemote(Token token, String currency, long amount, PaymentMethod paymentMethod, 
                        int expireMonth, int expireYear, String cardNumberTruncated, String remoteTransactionID);
    
    Token authorize(Token token, String currency, long amount, PaymentMethod method, String cardNumber, int cvd, int expireMonth, int expireYear);

}
