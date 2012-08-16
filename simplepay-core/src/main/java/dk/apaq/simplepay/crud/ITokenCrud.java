package dk.apaq.simplepay.crud;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface ITokenCrud extends Crud.Filterable<String, Token> {
    
    Token createNew(String certificate, PaymentGatewayType issuer);
    Token createNew(Card card);
}
