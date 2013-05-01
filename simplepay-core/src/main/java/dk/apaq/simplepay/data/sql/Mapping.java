
package dk.apaq.simplepay.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dk.apaq.framework.repository.jdbc.RowUnmapper;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

/**
 * Javadoc
 */
public class Mapping {

    public static final RowMapper<Merchant> MERCHANT_ROW_MAPPER = new RowMapper<Merchant>() {
        @Override
        public Merchant mapRow(ResultSet rs, int rowNum) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    public static final RowUnmapper<Merchant> MERCHANT_ROW_UNMAPPER = new RowUnmapper<Merchant>() {
        @Override
        public Map<String, Object> mapColumns(Merchant t) {
            Map map = new HashMap();
            return map;
        }
    };
    
    public static final RowMapper<Token> TOKEN_ROW_MAPPER = new RowMapper<Token>() {
        @Override
        public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    public static final RowUnmapper<Token> TOKEN_ROW_UNMAPPER = new RowUnmapper<Token>() {
        @Override
        public Map<String, Object> mapColumns(Token t) {
            Map map = new HashMap();
            return map;
        }
    };

    public static final RowMapper<Transaction> TRANSACTION_ROW_MAPPER = new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    public static final RowUnmapper<Transaction> TRANSACTION_ROW_UNMAPPER = new RowUnmapper<Transaction>() {
        @Override
        public Map<String, Object> mapColumns(Transaction t) {
            Map map = new HashMap();
            return map;
        }
    };
    
}
