package dk.apaq.simplepay.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.apaq.framework.common.beans.finance.PaymentInstrument;
import dk.apaq.framework.repository.jdbc.RowUnmapper;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.springframework.jdbc.core.RowMapper;

/**
 * Javadoc
 */
public final class PaymentGatewayAccessMapping {

    public static final String FIELD_PAYMENT_GATEWAY = "paymentGateway";
    public static final String FIELD_ACQUIRER_REF_ID = "acquirerRefId";
    public static final String FIELD_DATE_CHANGED = "dateChanged";
    public static final String FIELD_DATE_CREATED = "dateCreated";
    public static final String FIELD_VALID_INSTRUMENTS = "validInstruments";

    private PaymentGatewayAccessMapping() { /* EMPTY */ }

    public static class Mapper implements RowMapper<PaymentGatewayAccess> {

        @Override
        public PaymentGatewayAccess mapRow(ResultSet rs, int rowNum) throws SQLException {
            EPaymentGateway gateway = EPaymentGateway.valueOf(rs.getString(FIELD_PAYMENT_GATEWAY));
            PaymentGatewayAccess access = new PaymentGatewayAccess(gateway, rs.getString(FIELD_ACQUIRER_REF_ID));
            access.setDateChanged(rs.getDate(FIELD_DATE_CHANGED));
            access.setDateCreated(rs.getDate(FIELD_DATE_CREATED));

            String validInstruments = rs.getString(FIELD_VALID_INSTRUMENTS);
            List<PaymentInstrument> list = new ArrayList<PaymentInstrument>();
            if (validInstruments != null && !"".equals(validInstruments)) {
                for (String instrument : validInstruments.split(",")) {
                    list.add(PaymentInstrument.valueOf(instrument));
                }
            }
            access.setSpecificValidInstruments(list);
            return access;
        }
    }

    public static class Unmapper implements RowUnmapper<PaymentGatewayAccess> {

        @Override
        public Map<String, Object> mapColumns(PaymentGatewayAccess t) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(FIELD_ACQUIRER_REF_ID, t.getAcquirerRefId());
            map.put(FIELD_DATE_CHANGED, t.getDateChanged());
            map.put(FIELD_DATE_CREATED, t.getDateCreated());
            map.put(FIELD_PAYMENT_GATEWAY, t.getPaymentGatewayType());
            map.put(FIELD_VALID_INSTRUMENTS, iterableToCommaSeparated(t.getSpecificValidInstruments()));
            return map;
        }

        private String iterableToCommaSeparated(Iterable iterable) {
            StringBuilder sb = new StringBuilder();
            for (Object o : iterable) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(o.toString());
            }
            return sb.toString();
        }
    }
}
