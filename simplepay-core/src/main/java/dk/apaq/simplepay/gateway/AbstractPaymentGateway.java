package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.IPayService;

/**
 *
 * @author michael
 */
public abstract class AbstractPaymentGateway implements IPaymentGateway {

    protected IPayService service;

    public void setService(IPayService service) {
        this.service = service;
    }
}
