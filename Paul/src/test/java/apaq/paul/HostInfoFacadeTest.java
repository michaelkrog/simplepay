/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apaq.paul;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import paul.host.HostFacade;

/**
 *
 * @author krog
 */
public class HostInfoFacadeTest extends TestCase {
    
    public HostInfoFacadeTest(String testName) {
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

    public void testGetHostInfoList() {
        try {
            HostFacade.getHostInfoList();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

}
