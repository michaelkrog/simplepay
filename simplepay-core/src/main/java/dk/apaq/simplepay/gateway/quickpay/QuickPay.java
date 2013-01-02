package dk.apaq.simplepay.gateway.quickpay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.common.EPaymentIntrument;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.gateway.IHasPaymentInformation;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Token;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class QuickPay implements IPaymentGateway, IHasPaymentInformation {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPay.class);
    private String apiUrl = "https://secure.quickpay.dk/api";
    private String formUrl = "https://secure.quickpay.dk/form/";
    private boolean testMode;
    private org.apache.http.client.HttpClient httpClient;
    private final static String protocolVersion = "4";

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }

    @Override
    public void authorize(Merchant merchant, PaymentGatewayAccess access, Card card, Money money, String orderId, String terminalId, ETokenPurpose purpose) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId) {
        //validateToken(token);
        try {
            LOG.debug("Cancelling transaction [transactionId={}]", transactionId);
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "cancel"));
            nvps.add(md5.getBasicNameValuePair("merchant", access.getAcquirerRefId()));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));

            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }

            md5.add(access.getAcquirerApiKey());
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

    public PaymentInformation getPaymentInformation(Token token) {
        validateToken(token);
        try {
            //LOG.debug("Retrieving information about transaction [transactionId={}]", token.getGatewayTransactionId());
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "status"));
            //nvps.add(md5.getBasicNameValuePair("merchant", token.getMerchant().getGatewayUserId()));
            //nvps.add(md5.getBasicNameValuePair("transaction", token.getGatewayTransactionId()));

            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }

            //md5.add(token.getMerchant().getGatewaySecret());
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
    public void capture(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        //validateToken(token);
        try {
            //LOG.debug("Capturing money for transaction [transactionId={}; amountInCents={}]", new Object[]{token.getGatewayTransactionId(), amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", access.getAcquirerRefId()));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("finalize", "1"));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));
            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }

            md5.add(access.getAcquirerApiKey());
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
    @Override
    public void renew(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        //validateToken(token);
        try {
            //LOG.debug("Renewing transaction [transaction={}; amountInCents={}]", new Object[]{token.getGatewayTransactionId(), amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "renew"));
            nvps.add(md5.getBasicNameValuePair("merchant", access.getAcquirerRefId()));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));

            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }

            md5.add(access.getAcquirerApiKey());
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

    @Override
    public void refund(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        //validateToken(token);
        try {
            LOG.debug("Refunding transaction [transaction={}; amountInCents={}]", new Object[]{transactionId, amountInCents});
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "refund"));
            nvps.add(md5.getBasicNameValuePair("merchant", access.getAcquirerRefId()));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("transaction", transactionId));

            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }

            md5.add(access.getAcquirerRefId());
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

    

    public static ETransactionStatus getStatusFromState(int state) {
        switch (state) {
            case 0:
                return ETransactionStatus.Cancelled;
            case 1:
                return ETransactionStatus.Authorized;
            case 3:
                return ETransactionStatus.Charged;
            case 5:
                return ETransactionStatus.Cancelled;
            case 7:
                return ETransactionStatus.Refunded;
            default:
                return null;
        }
    }

    public static EPaymentIntrument getCardTypeFromString(String type) {
        if ("american-express".equals(type) || "american-express-dk".equals(type)) {
            return EPaymentIntrument.American_Express;
        }

        if ("dankort".equals(type)) {
            return EPaymentIntrument.Dankort;
        }

        if ("diners-express".equals(type) || "diners-express-dk".equals(type)) {
            return EPaymentIntrument.Diners;
        }

        if ("jcb".equals(type)) {
            return EPaymentIntrument.Jcb;
        }

        if ("mastercard".equals(type) || "mastercard-dk".equals(type)) {
            return EPaymentIntrument.Mastercard;
        }

        if ("visa".equals(type) || "visa-dk".equals(type)) {
            return EPaymentIntrument.Visa;
        }

        if ("visa-electron".equals(type) || "visa-electron-dk".equals(type)) {
            return EPaymentIntrument.Visa_Electron;
        }
        return null;
    }

    public static String getStringFromCardType(EPaymentIntrument type) {
        if (type == null) {
            return null;
        }

        switch (type) {
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

        if ("000".equals(qpstat)) {
            return;
        } else if ("001".equals(qpstat)) {
            throw new PaymentException("001: " + qpstatmsg + ". Rejected by acquirer.");
        } else if ("002".equals(qpstat)) {
            throw new PaymentException("002: " + qpstatmsg + ". Communication error.");
        } else if ("003".equals(qpstat)) {
            throw new PaymentException("003: " + qpstatmsg + ". Card expired.");
        } else if ("004".equals(qpstat)) {
            throw new PaymentException("004: " + qpstatmsg + ". Transition is not allowed for transaction current state.");
        } else if ("005".equals(qpstat)) {
            throw new PaymentException("005: " + qpstatmsg + ". Authorization is expired.");
        } else if ("006".equals(qpstat)) {
            throw new PaymentException("006: " + qpstatmsg + ". Error reported by acquirer.");
        } else if ("007".equals(qpstat)) {
            throw new PaymentException("007: " + qpstatmsg + ". Error reported by QuickPay.");
        } else if ("008".equals(qpstat)) {
            throw new PaymentException("008: " + qpstatmsg + ". Error in request data.");
        } else if ("009".equals(qpstat)) {
            throw new PaymentException("009: " + qpstatmsg + ". Payment aborted by shopper.");
        } else {
            throw new PaymentException("Unknown status. [status=" + qpstat + "]");
        }

    }

    private void validateToken(Token token) {
        if (token == null) {
            throw new NullPointerException("token was null.");
        }

        if (token.getMerchant() == null) {
            throw new IllegalArgumentException("token does not have merchant specified which is required.");
        }
    }
}
