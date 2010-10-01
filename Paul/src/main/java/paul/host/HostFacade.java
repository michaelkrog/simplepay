/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paul.host;

import apaq.paul.HostInfoComposer;
import apaq.paul.HostInfoParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.javavfs.Directory;
import org.javavfs.File;
import paul.file.FileSystemFacade;

/**
 *
 * @author krog
 */
public class HostFacade {

    public static List<HostInfo> getHostInfoList() throws IOException{
        Directory root = FileSystemFacade.getFileSystem().getRoot();
        Directory config = root.getDirectory("Config", true);
        Directory web = config.getDirectory("Web", true);
        File hostsfile = web.getFile("Hosts.json", true);

        if(hostsfile.getLength()==0){
            //We just created the file.
            //Make an empty file
            OutputStream os = null;
            try{
                os = hostsfile.getOutputStream();
                HostInfoComposer composer = new HostInfoComposer();
                composer.composteHostInfo(new ArrayList<HostInfo>(), os);
            } finally{
                if(os!=null)
                    os.close();
            }
        }
        
        InputStream is = null;
        
        try{
            is = hostsfile.getInputStream();
            HostInfoParser parser = new HostInfoParser();
            return parser.parseHostInfo(is);
        } finally {
            if(is!=null)
                is.close();
        }

    }
}
