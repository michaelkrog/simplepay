/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apaq.paul;

import paul.host.HostInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.javavfs.File;

/**
 *
 * @author krog
 */
public class HostInfoComposer {
    public void composteHostInfo(List<HostInfo> infolist, OutputStream outputStream) throws IOException{
        Gson gson = new Gson();
        gson.toJson(infolist, new OutputStreamWriter(outputStream));
    }
}
