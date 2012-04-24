package dk.apaq.simplepay.gateway.quickpay;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.common.CardType;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class QuickPay implements PaymentGateway {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPay.class);

    //private Merchant merchant = null;
    private IPayService service;
    private String apiUrl = "https://secure.quickpay.dk/api";
    private String formUrl = "https://secure.quickpay.dk/form/";
    private boolean testMode;
    private org.apache.http.client.HttpClient httpClient;
    private final static String protocolVersion = "4";

    public void setService(IPayService service) {
        this.service = service;
    }

    public void setMerchant(Merchant merchant) {
        //this.merchant = merchant;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    private HttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }
    
    @Override
    public void cancel(Transaction transaction) {
        try {
            LOG.debug("Cancelling transaction [transactionId={}]", transaction.getGatewayTransactionId());
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "cancel"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("transaction", transaction.getGatewayTransactionId()));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            post.getEntity().writeTo(System.out);

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
            
        } catch (IOException ex) {
            LOG.error("Unable to cancel payment.", ex);
            throw new PaymentException("Unable to cancel payment.", ex);
        } 
        
    }

    public PaymentInformation getPaymentInformation(Transaction transaction) {
        try {
            LOG.debug("Retrieving information about transaction [transactionId={}]", transaction.getGatewayTransactionId());
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "status"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("transaction", transaction.getGatewayTransactionId()));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            
            QuickPayResult qpresult = new QuickPayResult(result);
            
            List<PaymentInformation.HistoryEntry> history = new ArrayList();
            
            return new PaymentInformation(getStatusFromState(Integer.parseInt(qpresult.getParameter("state"))),
                                            history, 
                                            qpresult.getParameter("ordernumber"), 
                                            Integer.parseInt(qpresult.getParameter("amount")),
                                            qpresult.getParameter("currency"), 
                                            qpresult.getParameter("qpstat") + ": " + qpresult.getParameter("qpstatmsg"),
                                            qpresult.getParameter("merchant"),
                                            qpresult.getParameter("merchantemail"),
                                            qpresult.getParameter("transaction"),
                                            getCardTypeFromString(qpresult.getParameter("cardtype")));
            
            
        } catch (IOException ex) {
            LOG.error("Unable to get status for payment.", ex);
            throw new PaymentException("Unable to get status for payment.", ex);
        }
    }



    @Override
    public void capture(Transaction transaction, long amountInCents) {
        try {
            LOG.debug("Capturing money for transaction [transactionId={}; amountInCents={}]", new Object[]{transaction.getGatewayTransactionId(), amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("finalize", "1"));
            nvps.add(md5.getBasicNameValuePair("transaction", transaction.getGatewayTransactionId()));
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            LOG.error("Unable to capture payment.", ex);
            throw new PaymentException("Unable to capture payment.", ex);
        }
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        try {
            LOG.debug("Recurrings authorization for transaction [transactionId={}; orderNumber={}; amountInCents={}; currency={}; autoCapture={}]", 
                                                                    new Object[]{transactionId, orderNumber, amountInCents, currency, autocapture});
            
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("ordernumber", orderNumber));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("currency", currency));
            nvps.add(md5.getBasicNameValuePair("autocapture", autocapture ? "1" : "0"));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            LOG.error("Unable to create recurring payment.", ex);
            throw new PaymentException("Unable to create recurring payment.", ex);
        }
    }*/

    public void renew(Transaction transaction, long amountInCents) {
        try {
            LOG.debug("Renewing transaction [transaction={}; amountInCents={}]", new Object[]{transaction.getGatewayTransactionId(), amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "renew"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("transaction", transaction.getGatewayTransactionId()));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            LOG.error("Unable to renew payment.", ex);
            throw new PaymentException("Unable to renew payment.", ex);
        }
    }



    public void refund(Transaction transaction, long amountInCents) {
        try {
            LOG.debug("Refunding transaction [transaction={}; amountInCents={}]", new Object[]{transaction.getGatewayTransactionId(), amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "refund"));
            nvps.add(md5.getBasicNameValuePair("merchant", transaction.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("transaction", transaction.getGatewayTransactionId()));
            
            if(testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            
            md5.add(transaction.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            LOG.error("Unable to refund payment.", ex);
            throw new PaymentException("Unable to refund payment.", ex);
        }
    }

    public FormData generateFormdata(Transaction transaction, long amount, String currency, String okUrl, String cancelUrl, String callbackUrl, Locale locale) {
        FormData formData = new FormData();
        formData.setUrl("https://secure.quickpay.dk/form/");
        
        Map<String, String> map = formData.getFields();
        map.put("protocol", "4");
        map.put("msgtype", "authorize");
        map.put("merchant", transaction.getMerchant().getGatewayUserId());
        map.put("language", locale.getLanguage());
        map.put("ordernumber", transaction.getOrderNumber()); 
        map.put("amount", Long.toString(amount));
        map.put("currency", currency);
        map.put("continueurl", okUrl);
        map.put("cancelurl", cancelUrl);
        map.put("callbackurl", callbackUrl);
        map.put("autocapture", "0");
        map.put("autofee", "0");
        map.put("cardtypelock", "creditcard");
        map.put("description", transaction.getDescription());
        map.put("splitpayment", "1");
        
        //md5
        StringBuilder builder = new StringBuilder();
        for(String value : map.values()) {
            builder.append(value);
        }
        builder.append(transaction.getMerchant().getGatewaySecret());
        map.put("md5check", DigestUtils.md5Hex(builder.toString()));
        
        return formData;
    }
    
    public static TransactionStatus getStatusFromState(int state) {
        switch(state) {
            case 0:
                return TransactionStatus.New;
            case 1:
                return TransactionStatus.Authorized;
            case 3:
                return TransactionStatus.Charged;
            case 5:
                return TransactionStatus.Cancelled;
            case 7:
                return TransactionStatus.Refunded;
            default:
                return null;
        }
    }

    public static CardType getCardTypeFromString(String type) {
        if("american-express".equals(type) || "american-express-dk".equals(type)) {
            return CardType.American_Express;
        }
        
        if("dankort".equals(type)) {
            return CardType.Dankort;
        }
        
        if("diners-express".equals(type) || "diners-express-dk".equals(type)) {
            return CardType.Diners;
        }
        
        if("jcb".equals(type)) {
            return CardType.Jcb;
        }
        
        if("mastercard".equals(type) || "mastercard-dk".equals(type)) {
            return CardType.Mastercard;
        }
        
        if("visa".equals(type) || "visa-dk".equals(type)) {
            return CardType.Visa;
        }
        
        if("visa-electron".equals(type) || "visa-electron-dk".equals(type)) {
            return CardType.Visa_Electron;
        }
        return CardType.Unknown;
    }
    
    public static String getStringFromCardType(CardType type) {
        switch(type) {
            case American_Express:
                return "american-express";
            case Dankort:
                return "dankort";
            case Diners:
                return "diners";
            case Jcb:
                return "jcb";
            case Mastercard:
                return "mastercard";
            case Visa:
                return "visa";
            case Visa_Electron:
                return "visa-electron";
            default: 
                return null;
        }
        
    }
    
    public static void checkQuickpayResult(QuickPayResult result) {
        checkQuickpayResult(result.getParameter("qpstat"), result.getParameter("qpstatmsg"));
    }
    
    public static void checkQuickpayResult(String qpstat, String qpstatmsg) {
        
        if("000".equals(qpstat)) {
            return;
        } else if("001".equals(qpstat)) {
            throw new PaymentException("001: " + qpstatmsg + ". Rejected by acquirer.");
        } else if("002".equals(qpstat)) {
            throw new PaymentException("002: " + qpstatmsg + ". Communication error.");
        } else if("003".equals(qpstat)) {
            throw new PaymentException("003: " + qpstatmsg + ". Card expired.");
        } else if("004".equals(qpstat)) {
            throw new PaymentException("004: " + qpstatmsg + ". Transition is not allowed for transaction current state.");
        } else if("005".equals(qpstat)) {
            throw new PaymentException("005: " + qpstatmsg + ". Authorization is expired.");
        } else if("006".equals(qpstat)) {
            throw new PaymentException("006: " + qpstatmsg + ". Error reported by acquirer.");
        } else if("007".equals(qpstat)) {
            throw new PaymentException("007: " + qpstatmsg + ". Error reported by QuickPay.");
        } else if("008".equals(qpstat)) {
            throw new PaymentException("008: " + qpstatmsg + ". Error in request data.");
        } else if("009".equals(qpstat)) {
            throw new PaymentException("009: " + qpstatmsg + ". Payment aborted by shopper.");
        } else {
            throw new PaymentException("Unknown status. [status=" + qpstat + "]");
        }

    }

}

