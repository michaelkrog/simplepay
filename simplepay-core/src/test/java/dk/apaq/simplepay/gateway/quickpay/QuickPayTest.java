package dk.apaq.simplepay.gateway.quickpay;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayTransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.gateway.RemoteAuthPaymentGateway.FormData;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map.Entry;
import org.apache.commons.codec.digest.DigestUtils;
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

        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
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
        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
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

        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
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

        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
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

        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        quickPay.refund(token, 10000);
    }
    
    @Test
    public void testGenerateForm() {
        System.out.println("generateForm");
        Token token = new Token(PaymentGatewayType.Test, "ordernum", "");
        token.setGatewayTransactionId("123");
        token.setMerchant(merchant);
        
        String okUrl = "http://ok";
        String cancelUrl = "http://cancel";
        String callbackUrl = "http://callback";
        FormData formData = quickPay.generateFormdata(token, 10000, "DKK", okUrl, cancelUrl, callbackUrl, Locale.GERMANY);
        
        StringBuilder builder = new StringBuilder();
        for(Entry<String, String> entry : formData.getFields().entrySet()) {
            if(entry.getValue()!=null && !entry.getKey().equals("md5check")) builder.append(entry.getValue());
        }
        builder.append(merchant.getGatewaySecret());
        
        assertEquals(formData.getFields().get("continueurl"), okUrl);
        assertEquals(formData.getFields().get("md5check"), DigestUtils.md5Hex(builder.toString()));
    }
    
    @Test
    public void testStatusFromState() {

        assertEquals(PaymentGatewayTransactionStatus.New, QuickPay.getStatusFromState(0));
        assertEquals(PaymentGatewayTransactionStatus.Authorized, QuickPay.getStatusFromState(1));
        assertEquals(null, QuickPay.getStatusFromState(2));
        assertEquals(PaymentGatewayTransactionStatus.Charged, QuickPay.getStatusFromState(3));
        assertEquals(null, QuickPay.getStatusFromState(4));
        assertEquals(PaymentGatewayTransactionStatus.Cancelled, QuickPay.getStatusFromState(5));
        assertEquals(null, QuickPay.getStatusFromState(6));
        assertEquals(PaymentGatewayTransactionStatus.Refunded, QuickPay.getStatusFromState(7));
        assertEquals(null, QuickPay.getStatusFromState(8));
        
    }
    
    @Test
    public void testCardTypeFromString() {
        
        assertEquals(PaymentMethod.American_Express, QuickPay.getCardTypeFromString("american-express"));
        assertEquals(PaymentMethod.American_Express, QuickPay.getCardTypeFromString("american-express-dk"));
        assertEquals(PaymentMethod.Dankort, QuickPay.getCardTypeFromString("dankort"));
        assertEquals(PaymentMethod.Diners, QuickPay.getCardTypeFromString("diners-express"));
        assertEquals(PaymentMethod.Diners, QuickPay.getCardTypeFromString("diners-express-dk"));
        assertEquals(PaymentMethod.Jcb, QuickPay.getCardTypeFromString("jcb"));
        assertEquals(PaymentMethod.Mastercard, QuickPay.getCardTypeFromString("mastercard"));
        assertEquals(PaymentMethod.Mastercard, QuickPay.getCardTypeFromString("mastercard-dk"));
        assertEquals(PaymentMethod.Visa, QuickPay.getCardTypeFromString("visa"));
        assertEquals(PaymentMethod.Visa, QuickPay.getCardTypeFromString("visa-dk"));
        assertEquals(PaymentMethod.Visa_Electron, QuickPay.getCardTypeFromString("visa-electron"));
        assertEquals(PaymentMethod.Visa_Electron, QuickPay.getCardTypeFromString("visa-electron-dk"));
        assertEquals(PaymentMethod.Unknown, QuickPay.getCardTypeFromString(null));
    }
    
    @Test
    public void testStringFromCardtype() {
        assertEquals("american-express", QuickPay.getStringFromCardType(PaymentMethod.American_Express));
        assertEquals("dankort", QuickPay.getStringFromCardType(PaymentMethod.Dankort));
        assertEquals("diners", QuickPay.getStringFromCardType(PaymentMethod.Diners));
        assertEquals("jcb", QuickPay.getStringFromCardType(PaymentMethod.Jcb));
        assertEquals("mastercard", QuickPay.getStringFromCardType(PaymentMethod.Mastercard));
        assertEquals("visa", QuickPay.getStringFromCardType(PaymentMethod.Visa));
        assertEquals("visa-electron", QuickPay.getStringFromCardType(PaymentMethod.Visa_Electron));
        assertEquals(null, QuickPay.getStringFromCardType(PaymentMethod.Unknown));
    }
    
    @Test
    public void testCheckQuickpayResult() {
        try {
            QuickPay.checkQuickpayResult("001", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("002", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("003", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("004", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("005", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("006", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("007", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("008", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("009", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        try {
            QuickPay.checkQuickpayResult("dad", "error");
            fail("Should fail");
        } catch(PaymentException ex) {}
        
        
    }
    
    @Test
    public void testBeanPattern() {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        QuickPay q = new QuickPay();
        q.setHttpClient(mockHttpClient);
        q.setService(null);
        q.setTestMode(true);
        
        assertEquals(mockHttpClient, q.getHttpClient());
        assertEquals(true, q.isTestMode());
    }
    
    @Test
    public void testErrorHandling() throws IOException {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.execute(Mockito.any(HttpUriRequest.class))).thenThrow(new IOException("No internet connection"));
        
        Token token = new Token(PaymentGatewayType.QuickPay, "12", null);
        token.setMerchant(merchant);
        QuickPay q = new QuickPay();
        q.setHttpClient(mockHttpClient);
        
        try{
            q.capture(token, 100);
            fail("Should have thrown exception");
        } catch(PaymentException ex) { }
        
        try{
            q.cancel(token);
            fail("Should have thrown exception");
        } catch(PaymentException ex) { }
        
        try{
            q.refund(token, 100);
            fail("Should have thrown exception");
        } catch(PaymentException ex) { }
        
        try{
            q.renew(token, 200);
            fail("Should have thrown exception");
        } catch(PaymentException ex) { }
        
        try{
            q.getPaymentInformation(token);
            fail("Should have thrown exception");
        } catch(PaymentException ex) { }
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
