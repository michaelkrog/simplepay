package dk.apaq.simplepay.data;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface ITokenCrud extends Repository<Token, String> {
    
    Token createNew(Card card);
    void markExpired(Token token);
}
