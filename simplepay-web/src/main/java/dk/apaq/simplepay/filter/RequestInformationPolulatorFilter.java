package dk.apaq.simplepay.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import dk.apaq.simplepay.util.RequestInformationHelper;

/**
 *
 * @author krog
 */
public class RequestInformationPolulatorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { /* EMPTY */ }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestInformationHelper.setRemoteAddress(request.getRemoteAddr());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { /* EMPTY */ }
    
    
}
