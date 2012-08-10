package dk.apaq.simplepay.teaser;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author michael
 */
public class MessageBundle {
    private static final Map<String, MessageBundle> LOADED_BUNDLES = new HashMap<String, MessageBundle>();
    private Locale locale;
    private ResourceBundle bundle;
    

    private MessageBundle(Locale locale, String bundlePath) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle(bundlePath, locale);
    }

    public String get(String key, Object ... arguments) {
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, arguments);
    }
    
    public static synchronized MessageBundle get(Locale locale, String bundlePath) {
        String id = locale.toString()+":"+bundlePath;
        MessageBundle bundle = LOADED_BUNDLES.get(id);
        
        if(bundle == null) {
            bundle = new MessageBundle(locale, bundlePath);
            LOADED_BUNDLES.put(id, bundle);
        }
        return bundle;
    }
    
}
