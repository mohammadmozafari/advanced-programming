package backend;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for saving the data of the program such as settings, download files and etc
 *
 * @author Mohammad Mozafari
 */
public class StoredData implements Serializable
{
    //fields
    public static boolean finiteDownload;
    public static boolean repaintNeeded = false;
    public static int sameTimeDonwload, mainFrameWidth = 1300, mainFrameHeight = 700;
    public static HashMap<String, String> lookAndFeels;
    public static String downloadPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    public static String lookAndFeelName;
    private static ArrayList<DownloadFile> files = new ArrayList<>();
    private static ArrayList<DownloadFile> queues = new ArrayList<>();

    /**
     * Returns the download files based on the filter given.
     * @param tabType the tab type to filter download files
     * @return filtered files
     */
    public static ArrayList<DownloadFile> getFiles(Enums.TabType tabType)
    {
        ArrayList<DownloadFile> downloadFiles = new ArrayList<>();

        if (tabType == Enums.TabType.QUEUES)
            return queues;
        else if (tabType == Enums.TabType.DEFAULT)
            return files;

        for (int i = 0; i < files.size() ; i++)
        {
            if (files.get(i).getStatus() == Enums.DownloadingStatus.DOWNLOADING && tabType == Enums.TabType.PROCESSING)
                downloadFiles.add(files.get(i));
            else if (files.get(i).getStatus() == Enums.DownloadingStatus.COMPLETED && tabType == Enums.TabType.COMPLETED)
                downloadFiles.add(files.get(i));
        }

        return downloadFiles;
    }

    /**
     * Changes the look and feel of the program.
     */
    public static void setLookAndFeels()
    {
        lookAndFeels = new HashMap<>();
        for (UIManager.LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels())
        {
            if (lf.getName().equalsIgnoreCase("Nimbus"))
                lookAndFeels.put("Nimbus", lf.getClassName());
            else if (lf.getName().equalsIgnoreCase("Metal"))
                lookAndFeels.put("Metal", lf.getClassName());
        }
        lookAndFeels.put("System Default", UIManager.getSystemLookAndFeelClassName());
        lookAndFeelName = "System Default";
    }

    /**
     * Changes the aspects and dimesions of the frame.
     * @param width the width of the main frame
     * @param height the height of the main frame
     */
    public static void changeMainFrameDimensions(int width, int height)
    {
        mainFrameWidth = width;
        mainFrameHeight = height;
    }

    /**
     * Checks to see if any file is selected or not.
     * @return true if at least one is selected
     */
    public static boolean isAnyQueudFileSelected()
    {
        for (DownloadFile file : queues)
        {
            if (file.isSelected())
                return true;
        }
        return false;
    }
}
