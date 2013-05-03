package dk.apaq.simplepay.data.service;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.model.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author krog
 */
public interface ITokenService {
    public Token createNew(Card card);
    public Token markExpired(String token);
    public Iterable<Token> findAll();
    public Page<Token> findAll(Pageable pageable);
    public Token findOne(String token);
}
