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


    public Calendar getTime(){
        String time = null;
        try {
            time = getParameter("time");
            SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
            Date parse = format.parse(time);
            Calendar toReturn = Calendar.getInstance();
            toReturn.setTime(parse);
            return toReturn;
        } catch (ParseException e) {
            log.error("Cant parse time("+ time +") in xmlResponse("+ xmlResponse +")");
            return null;
        }
    }


    public static void main(String[] args) {
        QuickPayResult quickPayResult = new QuickPayResult("<response><msgtype>capture</msgtype><ordernumber/><amount>0</amount><currency/><time>110808174655</time><state/><qpstat>008</qpstat><qpstatmsg>md5check: e3cd4feeb2f548395af97eccae997dc4, hash: 2f95777e46868d362688700a72fcfc29</qpstatmsg><chstat/><chstatmsg/><merchant/><merchantemail/><transaction/><cardtype/><cardnumber/><splitpayment/><fraudprobability/><fraudremarks/><fraudreport/><md5check>104377e0749741e837facf34e5bf77f7</md5check></response>");
        System.out.println("quickPayResult.getParameter(\"msgtype\") = " + quickPayResult.getParameter("msgtype"));
        System.out.println("quickPayResult.getParameter(\"msgtype\") = " + quickPayResult.getParameter("amount"));
        System.out.println("quickPayResult.getTime() = " + quickPayResult.getTime().getTime());
    }


}

