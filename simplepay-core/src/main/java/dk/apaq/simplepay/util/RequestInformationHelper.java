package dk.apaq.simplepay.util;

/**
 *
 * @author michael
 */
public class RequestInformationHelper {

    private static final ThreadLocal<String> remoteAddress = new ThreadLocal<String>();

    private RequestInformationHelper() {
    }

    public static String getRemoteAddress() {
        String address = remoteAddress.get();
        return address == null ? "" : address;
    }

    public static void setRemoteAddress(String address) {
        remoteAddress.set(address);
    }
}
