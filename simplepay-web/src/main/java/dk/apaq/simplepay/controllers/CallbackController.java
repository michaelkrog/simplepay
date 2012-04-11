package dk.apaq.simplepay.controllers;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.security.MerchantUserDetailsHolder;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author krog
 */
@Controller
public class CallbackController {
    
    private static final Logger LOG = LoggerFactory.getLogger(CallbackController.class);
    
    public static final String[] QUICKPAY_KEYS = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    public static final String[] QUICKPAY_KEYS_SUBSCRIBE = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "cardexpire", "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    
    @Autowired
    private PayService service;
    
    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @RequestMapping(value="/{publicKey}/{token}", method=RequestMethod.POST, params="gateway=quickpay") 
    public void handleQuickpayEvent(MultipartHttpServletRequest request, @PathVariable String publicKey, @PathVariable String token) {
        LOG.debug("Payment event recieved");
        
        Merchant merchant = service.getMerchantByPublicKey(publicKey);
        String eventType = request.getParameter("msgtype");
        
        
        String[] keys = "subscribe".equals(eventType) ? QUICKPAY_KEYS_SUBSCRIBE : QUICKPAY_KEYS;
        
        if(!validate(request, request.getParameter("md5check"), merchant.getGatewaySecret(), keys)) {
            LOG.warn("Payment data is invalid!!! [merchantId={}; remoteIp={}]", merchant.getId(), request.getRemoteAddr());
            throw new InvalidRequestException("The data sent is not valid(checked against md5check).");
        }
        
                                                
        if (!"000".equals(request.getParameter("qpstat"))) {
            LOG.debug("Payment did not have a qpstat we handle. [qpstat={}]", request.getParameter("qpstat"));
            //We dont care about other requests
            return;
        }
        
        String orderNumber=request.getParameter("ordernumber");
        long amount;
        String currency = request.getParameter("currency");
        String transaction = request.getParameter("transaction");
        
        LOG.debug("Payment event type is " + eventType);
        
        try {
            amount = Long.parseLong(request.getParameter("amount"));
        } catch(NumberFormatException ex) {
            LOG.warn("Payment data was valid, but amount is not a valid number!!! [amount={}; remoteIp={}]", request.getParameter("amount"), request.getRemoteAddr());
            throw new InvalidRequestException("amount not a valid number [value="+request.getParameter("amount") +"]");
        }
        
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(PaymentGatewayType.QuickPay, merchant.getGatewayUserId(), merchant.getGatewaySecret());

        
        if("subscribe".equals(eventType)) {
            
            
        } 
        
        if("authorize".equals("eventType")) {
        
            /*
            String orderId = getOrderIdFromOrderNumber(sellerService, orderNumber);
            LOG.debug("Orderid found from orderNumber. [orderId={}; orderNumber={}]", orderId, orderNumber);
            Order order = sellerService.getOrders().read(orderId);
            CustomerRelationship customerRelationship = null;
            

            if(order.getBuyerId() != null) {
                LOG.debug("Order carried a customer. Loading customerRelationsShip");
                customerRelationship = sellerService.getCustomers().read(order.getBuyerId());
            }
        
            //Woohoo. :) User is really gonna pay - accept order if it isnt already accepted
            if(!order.getStatus().isConfirmedState()) {
                LOG.debug("Order was not already accepted. Changing it to accepted.");
                order.setStatus(OrderStatus.Accepted);
                order = sellerService.getOrders().update(order);
            }

            //take money
            LOG.debug("Charging money from customers card.");
            try {
                gateway.capture(amount.getAmountMinorLong(), request.getParameter("transaction"));
                LOG.debug("Money was charged from card.");
            } catch(PaymentException ex) {
                LOG.warn("Unable to charge money from card.", ex);
                throw ex;
            }

            LOG.debug("Persisting payment information..");

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setOrderId(orderId);
            payment.setPaymentType(PaymentType.Card);
            payment.setPaymentDetails(request.getParameter("cardtype") + ": " + request.getParameter("cardnumber"));
            sellerService.getPayments().create(payment);

            //Payments may have changed order properties - reload it
            order = sellerService.getOrders().read(orderId);

            if(!order.isPaid()) {
                LOG.warn("A payment went down on an order but was not marked as fully paid. [orderId={}; order total={}; payment amount={}]", 
                        new Object[]{order.getId(), order.getTotalWithTax(), amount});
            }*/
        }
    }
    
    private boolean validate(HttpServletRequest request, String md5check, String secret, String[] keys) {
        StringBuilder sb = new StringBuilder();
        for(String key : keys) {
            sb.append(request.getParameter(key));
        }
        sb.append(secret);
        return md5check.equals(DigestUtils.md5Hex(sb.toString()));
    }
}
