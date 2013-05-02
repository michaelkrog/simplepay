package dk.apaq.simplepay.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dk.apaq.framework.repository.jdbc.JdbcRepository;
import dk.apaq.framework.repository.jdbc.TableDescription;
import dk.apaq.framework.repository.jdbc.sql.SqlGenerator;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

/**
 * Javadoc
 */
public class MerchantRowMapper implements RowMapper<Merchant> {

    private final JdbcRepository<PaymentGatewayAccess, String> gatewayAccessRepository;

    public MerchantRowMapper(JdbcRepository<PaymentGatewayAccess, String> gatewayAccessRepository) {
        this.gatewayAccessRepository = gatewayAccessRepository;
    }
    
    @Override
    public Merchant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Merchant m = new Merchant();
        m.setId(rs.getString("id"));
        m.setCity(rs.getString("city"));
        m.setCountryCode(rs.getString("countryCode"));
        m.setDateChanged(rs.getDate("dateChanged"));
        m.setDateCreated(rs.getDate("dateCreated"));
        m.setEmail(rs.getString("email"));
        m.setName(rs.getString("name"));
        m.setPhone(rs.getString("phone"));
        m.setPostalCode(rs.getString("postalCode"));
        m.setStreet(rs.getString("street"));
        
        //TODO Find only those that belongs to the merchant
        m.getPaymentGatewayAccesses().addAll(gatewayAccessRepository.findAll());
        return m;
    }
}
