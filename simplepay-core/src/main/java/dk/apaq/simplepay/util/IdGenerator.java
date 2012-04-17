package dk.apaq.simplepay.util;

import java.util.UUID;

/**
 *
 * @author michael
 */
public class IdGenerator {
    
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
