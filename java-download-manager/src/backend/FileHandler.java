package backend;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This class has method used when working with files and directories.
 *
 * @author Mohammad Mozafari
 */
public class FileHandler
{
    /**
     * Opens a directory or file.
     * @param path the path to be opened.
     */
    public static void openPath(String path)
    {
        File file = new File(path);
        Desktop ds = Desktop.getDesktop();
        try
        {
            ds.open(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
