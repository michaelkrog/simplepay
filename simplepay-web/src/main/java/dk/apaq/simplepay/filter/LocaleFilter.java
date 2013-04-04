package dk.apaq.simplepay.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author michael
 */
public class LocaleFilter implements Filter {
    private String defaultLanguage = "en";
    private final List<String> supportedLanguages = new ArrayList<String>();
    private final List<String> ignoredPaths = new ArrayList<String>();

    private class LocaleWrappingRequest extends HttpServletRequestWrapper {

        private final Locale locale;

        public LocaleWrappingRequest(HttpServletRequest request, Locale locale) {
            super(request);
            this.locale = locale;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }
    }

    public LocaleFilter() {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String path = httpRequest.getServletPath();

            if (!isPathIgnored(path)) {

                if (httpRequest.getQueryString() != null) {
                    path += "?" + httpRequest.getQueryString();
                }

                Locale locale = getLocaleFromPath(path);
                if (locale == null) {
                    //Url has no indication of Locale - Get browsers locale
                    Locale userLocale = request.getLocale();

                    //if browsers locale is not default language but is a supported
                    //language, then redirect to that language
                    if (!defaultLanguage.equals(userLocale.getLanguage())
                            && isLanguageSupported(userLocale)) {
                        httpResponse.sendRedirect("/" + userLocale.getLanguage() + path);
                        return;
                    }
                }

                if (locale == null) {
                    //TODO Should get af default locale from LocaleUtil
                    locale = Locale.ENGLISH;

                }

                request = new LocaleWrappingRequest(httpRequest, locale);

            }
        }

        chain.doFilter(request, response);

    }

    public void init(FilterConfig config) throws ServletException {
        String languages = config.getInitParameter("languages");
        String defaultLanguage = config.getInitParameter("defaultLanguage");
        String ignoredPaths = config.getInitParameter("ignoredPaths");
        
        if(languages == null) {
            languages = "en";
        }
        
        if(defaultLanguage == null) {
            defaultLanguage = "en";
        }
        
        this.defaultLanguage = defaultLanguage;
        this.supportedLanguages.addAll(Arrays.asList(defaultLanguage.split(",")));
        
        if(ignoredPaths!=null) {
            this.ignoredPaths.addAll(Arrays.asList(ignoredPaths.split(",")));
        }
        
    }

    private Locale getLocaleFromPath(String path) {
        //The context of the users request revolve his wanted locale
        //if first path element if two-lettered and one of the supported locales
        //then we resolve the locale, remove the context and set the locale as property
        //if no matching path element is found, then default to english.
        String lang = "en";
        Locale locale = null;

        if (path != null && path.length() > 2) {
            String tmp = path;

            //It should according to standard start with a slash
            //We will remove it before splitting to array
            tmp = tmp.substring(1);

            String[] array = tmp.split("/");
            if (array[0].length() == 2) {
                lang = array[0];

                locale = getLocaleFromLanguageCode(lang);
            }

        }
        return locale;
    }

    private Locale getLocaleFromCountryCode(String country) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getCountry().equals(country)) {
                return locale;
            }
        }
        return null;
    }

    private Locale getLocaleFromLanguageCode(String language) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equals(language)) {
                return locale;
            }
        }
        return null;
    }

    private boolean isLanguageSupported(Locale locale) {
        if (locale == null) {
            return false;
        }
        for (String language : supportedLanguages) {
            if (language.equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathIgnored(String path) {
        for(String current : ignoredPaths) {
            if(path.startsWith(current)) {
                return true;
            }
        }
        return false;
    }

}
