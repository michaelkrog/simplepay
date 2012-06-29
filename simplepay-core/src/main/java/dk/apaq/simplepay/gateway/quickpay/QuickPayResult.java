package dk.apaq.simplepay.gateway.quickpay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class QuickPayResult {

    private static final Logger log = LoggerFactory.getLogger(QuickPayResult.class);

    private String xmlResponse = null;

    protected QuickPayResult(String xmlResponse){
        log.debug("Got response: " + xmlResponse);
        this.xmlResponse = xmlResponse;
    }


    public String getParameter(String nameOfParameter){
        int startIndex = xmlResponse.indexOf("<" + nameOfParameter + ">");
        if(startIndex > 0){
            int endIndex = xmlResponse.indexOf("</" + nameOfParameter + ">", startIndex);
            if(endIndex > startIndex){
                return xmlResponse.substring(startIndex + nameOfParameter.length() + 2, endIndex);
            }
        }
        return null;
    }

}

