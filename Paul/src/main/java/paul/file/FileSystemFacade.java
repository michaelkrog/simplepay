/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paul.file;

import java.io.File;
import org.javavfs.FileSystem;
import org.javavfs.impl.nativefs.NativeFileSystem;

/**
 *
 * @author krog
 */
public class FileSystemFacade {

    private static FileSystem fileSystem = null;

    public static synchronized FileSystem getFileSystem(){
        if(fileSystem==null)
            fileSystem = new NativeFileSystem(new File("data"));
        return fileSystem;
    }
}
