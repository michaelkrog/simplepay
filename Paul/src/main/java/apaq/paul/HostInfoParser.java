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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.javavfs.File;

/**
 *
 * @author krog
 */
public class HostInfoParser {
    public List<HostInfo> parseHostInfo(InputStream inputStream) throws IOException{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<HostInfo>>(){}.getType();
        return gson.fromJson(new InputStreamReader(inputStream), collectionType);
    }
}
