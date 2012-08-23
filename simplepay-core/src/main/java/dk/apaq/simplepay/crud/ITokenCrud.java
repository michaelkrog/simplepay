package dk.apaq.simplepay.crud;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface ITokenCrud extends Crud.Filterable<String, Token> {
    
    Token createNew(Card card);
    void markExpired(Token token);
}
