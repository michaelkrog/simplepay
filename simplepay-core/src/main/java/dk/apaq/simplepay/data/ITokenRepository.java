package dk.apaq.simplepay.data;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface ITokenRepository extends Repository<Token, String> {

    /**
     * Creates a new token.
     * @param card The card to as payment data for this token.
     * @return The new token.
     */
    Token createNew(Card card);

    /**
     * Marks a token as expired.
     * @param token The token to mark as expired.
     */
    void markExpired(Token token);
}
