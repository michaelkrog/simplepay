package dk.apaq.simplepay.gateway;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class PaymentGatewayManager {
    
    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayManager.class);
    private Map<String, Class> gatewayMap;
    
    @PostConstruct
    protected void init() {
        if(gatewayMap == null) {
            gatewayMap = new HashMap<String, Class>();
        }
    }

    public void setGatewayMap(Map<String, Class> gatewayMap) {
        this.gatewayMap = gatewayMap;
    }
    
    public PaymentGateway createPaymentGateway(PaymentGatewayType type, String merchantId, String secret) {
        Class clazz = gatewayMap.get(type.name());
        if(clazz == null) {
            throw new NullPointerException("No gateway by that type [type="+type+"]");
        }
        
        PaymentGateway paymentGateway = null;
        
        try {
            paymentGateway = (PaymentGateway) clazz.newInstance();
        } catch (Exception ex) {
            LOG.error("Unable to create instance of PaymentGateway.", ex);
            throw new NullPointerException("No gateway by that type because an error occured when trying to create it. [type="+type+"]");
        }
        
        paymentGateway.setMerchantId(merchantId);
        paymentGateway.setMerchantSecret(secret);
        
        return paymentGateway;
                
    }
}
