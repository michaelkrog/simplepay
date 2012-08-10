package dk.apaq.simplepay.teaser;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author michael
 */
public class MessageBundle {
    private Locale locale;
    private ResourceBundle bundle;

    public MessageBundle(Locale locale, String bundlePath) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle(bundlePath, locale);
    }

    public String getMessage(String key, Object ... arguments) {
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, arguments);
    }
    
}
