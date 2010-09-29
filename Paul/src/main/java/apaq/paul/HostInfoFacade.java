/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apaq.paul;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
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
public class HostInfoFacade {

    public static List<HostInfo> parseHostInfo(File file) throws IOException{
        Type collectionType = new TypeToken<Collection<HostInfo>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(file.getInputStream()), collectionType);
    }
}
