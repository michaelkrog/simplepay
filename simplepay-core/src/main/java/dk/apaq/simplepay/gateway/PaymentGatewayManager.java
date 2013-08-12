package dk.apaq.simplepay.gateway;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import dk.apaq.simplepay.PaymentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class PaymentGatewayManager {

    @Autowired
    private PaymentContext service;
    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayManager.class);
    private Map<String, IPaymentGateway> gatewayMap;

    @PostConstruct
    protected void init() {
        if (gatewayMap == null) {
            gatewayMap = new HashMap<String, IPaymentGateway>();
        }
    }

    public void setGatewayMap(Map<String, IPaymentGateway> gatewayMap) {
        this.gatewayMap = gatewayMap;
    }

    public IPaymentGateway getPaymentGateway(EPaymentGateway type) {
        IPaymentGateway gateway = gatewayMap.get(type.name());
        if (gateway == null) {
            throw new IllegalArgumentException("No gateway by that type [type=" + type + "]");
        }

        return gateway;

    }
}
