package dk.apaq.simplepay.gateway.quickpay;

import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGatewayTransactionStatus;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class QuickPayTest {

    @Before
    public void init() {
        merchant = new Merchant();
        merchant.setGatewayUserId("rwer");
    }
    
    private Merchant merchant;
    private QuickPay quickPay = new QuickPay();

    /**
     * Test of cancel method, of class QuickPay.
     */
    @Test
    public void testCancel() throws IOException {
        System.out.println("cancel");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<response>"
                + "<qpstat>000</qpstat>"
                + "<qpstatmsg>OK</qpstatmsg>"
                + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);

        Token token = new Token();
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        quickPay.cancel(token);

    }

    /**
     * Test of status method, of class QuickPay.
     */
    @Test
    public void testStatus() throws IOException {
        System.out.println("status");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<response>"
                + "<msgtype>status</msgtype>"
                + "<ordernumber>RTEST-120283102676</ordernumber>"
                + "<amount>234</amount>"
                + "<currency>DKK</currency>"
                + "<time>080212164347</time>"
                + "<state>7</state>"
                + "<chstat>000</chstat>"
                + "<qpstat>000</qpstat>"
                + "<qpstatmsg>OK</qpstatmsg>"
                + "<merchant>Pil.dk Test konto</merchant>"
                + "<merchantemail>test1@pil.dk</merchantemail>"
                + "<transaction>968</transaction>"
                + "<cardtype>Dankort</cardtype>"
                + "<splitpayment>1</splitpayment>"
                + "<md5check>8fc148ee35f589f2db420655a6771e1c</md5check>"
                + "<history>"
                + "     <msgtype>authorize</msgtype>"
                + "     <amount>234</amount>"
                + "     <state>1</state>"
                + "     <time>080212164346</time>"
                + "     <qpstat>000</qpstat>"
                + "     <chstat>000</chstat>"
                + "</history>"
                + "<history>"
                + "     <msgtype>capture</msgtype>"
                + "     <amount>234</amount>"
                + "     <state>3</state>"
                + "     <time>080212164347</time>"
                + "     <qpstat>000</qpstat>"
                + "     <chstat>000</chstat>"
                + "</history>"
                + "<history>"
                + "     <msgtype>refund</msgtype>"
                + "     <amount>234</amount>"
                + "     <state>7</state>"
                + "     <time>080212164347</time>"
                + "     <qpstat>000</qpstat>"
                + "     <chstat>000</chstat>"
                + "</history>"
                + "</response>";

        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);
        Token token = new Token();
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        PaymentInformation status = quickPay.getPaymentInformation(token);

        assertEquals(PaymentGatewayTransactionStatus.Refunded, status.getTransationStatus());
    }

    /**
     * Test of capture method, of class QuickPay.
     */
    @Test
    public void testCapture() throws IOException {
        System.out.println("capture");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<response>"
                + "<qpstat>000</qpstat>"
                + "<qpstatmsg>OK</qpstatmsg>"
                + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);

        Token token = new Token();
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        quickPay.capture(token, 10000);
    }

    /**
     * Test of recurring method, of class QuickPay.
     */
    /*Test
    public void testRecurring() throws IOException {
    System.out.println("recurring");
    String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    + "<response>"
    + "<qpstat>000</qpstat>"
    + "<qpstatmsg>OK</qpstatmsg>"
    + "</response>";
    HttpResponse response = prepareResponse(200, responseBody);
    HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
    Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);
    
    quickPay.setHttpClient(mockHttpClient);
    
    Transaction t = new Transaction();
    t.setGatewayTransactionId("123");
    quickPay.recurring("121212",10000,"DKK", false, "123");
    }*/
    /**
     * Test of renew method, of class QuickPay.
     */
    @Test
    public void testRenew() throws IOException {
        System.out.println("renew");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<response>"
                + "<qpstat>000</qpstat>"
                + "<qpstatmsg>OK</qpstatmsg>"
                + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);

        Token token = new Token();
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        quickPay.renew(token, 10000);
    }

    /**
     * Test of refund method, of class QuickPay.
     */
    @Test
    public void testRefund() throws IOException {
        System.out.println("refund");
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<response>"
                + "<qpstat>000</qpstat>"
                + "<qpstatmsg>OK</qpstatmsg>"
                + "</response>";
        HttpResponse response = prepareResponse(200, responseBody);
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(response);

        quickPay.setHttpClient(mockHttpClient);

        Token token = new Token();
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        quickPay.refund(token, 10000);
    }

    private HttpResponse prepareResponse(int expectedResponseStatus, String expectedResponseBody) {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), expectedResponseStatus, ""));
        response.setStatusCode(expectedResponseStatus);
        try {
            response.setEntity(new StringEntity(expectedResponseBody));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return response;
    }
}
