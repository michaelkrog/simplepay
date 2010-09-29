/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apaq.paul;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author krog
 */
public class HostInfoParserTest extends TestCase {
    
    public HostInfoParserTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testHostFacade() {
        

        InputStream inputStream = HostInfoParserTest.class.getResourceAsStream("/hosts.json");
        HostInfoParser infoParser = new HostInfoParser();
        List<HostInfo> list=null;
        try {
            list = infoParser.parseHostInfo(inputStream);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        System.out.println("count: "+list.size());
    }

}
