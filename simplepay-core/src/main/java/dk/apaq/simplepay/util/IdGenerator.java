package dk.apaq.simplepay.util;

import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class IdGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);
    private static final String IGNORED_LOCALHOST = "127.0.0.1";
    private static final Inet4Address INSTANCE_ADDRESS = getFirstRealAddress();
    private static final byte[] INSTANCE_ADDRESS_BYTES = INSTANCE_ADDRESS.getAddress();
    private static final SecureRandom random = new SecureRandom(INSTANCE_ADDRESS_BYTES);
    private static final long YEAR_2010 = new Date(100, 0, 1).getTime();
    private static final long MILLISPERDAY = 86400000;
    private static short primaryCounter;
    
    private static Inet4Address getFirstRealAddress() {
        try {
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements();) {
                NetworkInterface e = n.nextElement();
                LOG.debug("Interface: " + e.getName());
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements();) {
                    InetAddress addr = a.nextElement();
                    if (addr instanceof Inet4Address && !IGNORED_LOCALHOST.equals(addr.getHostAddress())) {
                        return (Inet4Address) addr;
                    }
                }
            }
            throw new IllegalStateException("No valid interface found.");
        } catch (SocketException ex) {
            throw new IllegalStateException("Error looping through interfaces found.", ex);
        }
    }

    private static synchronized short getCounter() {
        return primaryCounter++;

    }

    public static String generateUniqueId() {
        return generateUniqueId(null);
    }

    public static String generateUniqueId(String prefix) {
        long timestamp = (System.currentTimeMillis() - YEAR_2010) / 1000;
        byte[] timebuffer = new byte[8];
        short count = getCounter();

        //CHECKSTYLE:OFF
        timebuffer[0] = (byte) (timestamp >>> 56);
        timebuffer[1] = (byte) (timestamp >>> 48);
        timebuffer[2] = (byte) (timestamp >>> 40);
        timebuffer[3] = (byte) (timestamp >>> 32);
        timebuffer[4] = (byte) (timestamp >>> 24);
        timebuffer[5] = (byte) (timestamp >>> 16);
        timebuffer[6] = (byte) (timestamp >>> 8);
        timebuffer[7] = (byte) (timestamp >>> 0);


        int usedTimebufferBytes = timebuffer.length;
        for (int i = 0; i < 7; i++) {
            if (timebuffer[i] != 0) {
                usedTimebufferBytes = timebuffer.length - i;
                break;
            }
        }

        byte[] randombuffer = new byte[4];
        random.nextBytes(randombuffer);

        int index = 0;
        byte[] buffer = new byte[randombuffer.length + usedTimebufferBytes + 3];
        int offset = 8 - usedTimebufferBytes;
        for (int i = 0; i < usedTimebufferBytes; i++) {
            buffer[index++] = timebuffer[offset + i];
        }

        for (int i = 0; i < randombuffer.length; i++) {
            buffer[index++] = randombuffer[i];
        }
        
        buffer[index++] = INSTANCE_ADDRESS_BYTES[3];
        buffer[index++] = (byte) (count >> 8);
        buffer[index++] = (byte) (count >> 0);

        //mix some bytes to scramble more
        byte tmp = buffer[3];
        buffer[3] = buffer[6];
        buffer[6] = tmp;

        tmp = buffer[5];
        buffer[5] = buffer[8];
        buffer[8] = tmp;
        
        tmp = buffer[2];
        buffer[2] = buffer[7];
        buffer[7] = tmp;

        //CHECKSTYLE:ON

        String result = Base64.encodeBase64URLSafeString(buffer);
        int equalsSignIndex = result.indexOf("=");
        if (equalsSignIndex >= 0) {
            result = result.substring(0, equalsSignIndex);
        }

        if (prefix != null) {
            result = prefix + "_" + result;
        }



        LOG.debug("Generated id. [id=;prefix=]", result, prefix);

        return result;
    }
}
