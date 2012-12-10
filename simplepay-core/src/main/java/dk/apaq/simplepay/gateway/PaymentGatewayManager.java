package dk.apaq.simplepay.gateway;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import dk.apaq.simplepay.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class PaymentGatewayManager {

    @Autowired
    private IPayService service;
    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayManager.class);
    private Map<String, Class> gatewayMap;

    @PostConstruct
    protected void init() {
        if (gatewayMap == null) {
            gatewayMap = new HashMap<String, Class>();
        }
    }

    public void setGatewayMap(Map<String, Class> gatewayMap) {
        this.gatewayMap = gatewayMap;
    }

    public IPaymentGateway createPaymentGateway(EPaymentGateway type) {
        Class<IPaymentGateway> clazz = gatewayMap.get(type.name());
        if (clazz == null) {
            throw new NullPointerException("No gateway by that type [type=" + type + "]");
        }

        IPaymentGateway paymentGateway = null;

        try {
            paymentGateway = clazz.newInstance();
        } catch (Exception ex) {
            LOG.error("Unable to create instance of PaymentGateway.", ex);
            throw new NullPointerException("No gateway by that type because an error occured when trying to create it. [type=" + type + "]");
        }

        paymentGateway.setService(service);

        return paymentGateway;

    }
}
