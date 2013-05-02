
package dk.apaq.simplepay.data.sql;

import java.util.HashMap;
import java.util.Map;

import dk.apaq.framework.repository.jdbc.RowUnmapper;
import dk.apaq.framework.repository.jdbc.TableDescription;
import dk.apaq.framework.repository.jdbc.sql.SqlGenerator;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Javadoc
 */
public class MerchantRowUnmapper implements RowUnmapper<Merchant> {

    private final PaymentGatewayAccessMapping.Unmapper gatewayAccessUnmapper = new PaymentGatewayAccessMapping.Unmapper();
    private final JdbcOperations jdbcOperations;
    private final SqlGenerator sqlGenerator;
    private final TableDescription paymentGatewayAccessTable;
    private final PaymentGatewayAccessMapping.Mapper gatewayAccessMapper = new PaymentGatewayAccessMapping.Mapper();

    public MerchantRowUnmapper(JdbcOperations jdbcOperations, SqlGenerator sqlGenerator, TableDescription paymentGatewayAccessTable) {
        this.jdbcOperations = jdbcOperations;
        this.sqlGenerator = sqlGenerator;
        this.paymentGatewayAccessTable = paymentGatewayAccessTable;
    }

    public Map<String, Object> mapColumns(Merchant t) {
        Map map = new HashMap();
        map.put("id", t.getId());
        map.put("city", t.getCity());
        map.put("countryCode", t.getCountryCode());
        map.put("dateChanged", t.getDateChanged());
        map.put("dateCreated", t.getDateCreated());
        map.put("email", t.getEmail());
        map.put("name", t.getName());
        map.put("phone", t.getPhone());
        map.put("postalCode", t.getPostalCode());
        map.put("street", t.getStreet());
        
        for(PaymentGatewayAccess access : t.getPaymentGatewayAccesses()) {
            Map<String, Object> accessMap = gatewayAccessUnmapper.mapColumns(access);
            jdbcOperations.
        }
        return map;
    }
}
