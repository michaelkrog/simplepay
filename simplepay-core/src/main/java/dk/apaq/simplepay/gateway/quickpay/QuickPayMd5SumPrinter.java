package dk.apaq.simplepay.gateway.quickpay;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class QuickPayMd5SumPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPayMd5SumPrinter.class);
    private StringBuilder builder = new StringBuilder();

    public String printHtmlHidden(String name, String input) {
        builder.append(input);
        return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + input + "\">";
    }

    public BasicNameValuePair getBasicNameValuePair(String name, String value) {
        add(value);
        return new BasicNameValuePair(name, value);
    }

    public void add(String input) {
        //md.update(input.getBytes());
        builder.append(input);
    }

    public String getMD5Result() {
        String fullString = builder.toString();
        String toReturn = DigestUtils.md5Hex(fullString);
        LOG.debug("getMD5Result: " + fullString + "=" + toReturn);
        return toReturn;
    }

    public String getSHAResult() {
        String fullString = builder.toString().replaceAll("\\s", "");
        String toReturn = DigestUtils.shaHex(fullString);
        LOG.debug("getSHAResult: " + fullString + "=" + toReturn);
        return toReturn;
    }

    public void clear() {
        builder.setLength(0);
    }
}
