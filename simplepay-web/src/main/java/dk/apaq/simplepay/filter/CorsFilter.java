package dk.apaq.simplepay.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author michael
 */
public class CorsFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if(response instanceof HttpServletResponse) {
            HttpServletResponse hRes = (HttpServletResponse) response;
            hRes.setHeader("Access-Control-Allow-Origin", "*");
            hRes.setHeader("Access-Control-Allow-Methods", "GET,POST");
            hRes.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        }
        chain.doFilter(request, response);
       
    }


    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void destroy() { }

}
