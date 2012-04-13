package dk.apaq.simplepay.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author michael
 */
public class CorsFilter implements Filter {
    
    public class FakeBasicAuthRequestWrapper extends HttpServletRequestWrapper {

        private String key;

        public FakeBasicAuthRequestWrapper(String key, HttpServletRequest hsr) {
            super(hsr);
            this.key = key;
        }
        
        
        @Override
        public String getHeader(String string) {
            if(string.equals("Authorization")) {
                return "Basic " + Base64.encodeBase64String((key + ":").getBytes());
            }
            return super.getHeader(string);
        }
        
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if(response instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpRes = (HttpServletResponse) response;
            
            boolean preflight = isPreflight(httpReq);
            String origin = httpReq.getHeader("Origin");
            httpRes.setHeader("Access-Control-Allow-Origin", origin);
            httpRes.setHeader("Access-Control-Allow-Credentials", "true");
            httpRes.setHeader("Access-Control-Allow-Headers", "SimplePayKey");
            httpRes.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            
            if(preflight) return;
            
            //Also a hack - convert SimplePayKey into basic auth :)
            String key = httpReq.getHeader("SimplePayKey");
            if(key!=null) {
                httpReq = new FakeBasicAuthRequestWrapper(key, httpReq);
            }
            
            chain.doFilter(httpReq, httpRes);
        } else {
            chain.doFilter(request, response);
        }
        
        
            
        
        
       
    }
    
    private boolean isPreflight(HttpServletRequest request) {
        return request.getHeader("Access-Control-Request-Method") != null;
    }
    
    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void destroy() { }

}
