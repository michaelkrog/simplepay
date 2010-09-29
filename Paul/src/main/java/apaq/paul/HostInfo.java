/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apaq.paul;

import java.net.URL;
import java.util.List;

/**
 *
 * @author krog
 */
public class HostInfo {

    private String name;
    private List<URL> listeners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<URL> getListeners() {
        return listeners;
    }

}
