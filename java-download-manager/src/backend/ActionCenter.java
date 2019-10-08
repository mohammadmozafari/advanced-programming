package backend;

import javax.swing.*;
import java.util.ArrayList;

public class ActionCenter
{
    /**
     * Saves the settings in StoredData class.
     * @param path where files are saved
     * @param finite same time download
     * @param sameTimeDownload number of downloads at the same time
     * @param lookAndFeel the look and feel of the program
     */
    public static void saveSettings(String path, boolean finite, int sameTimeDownload, String lookAndFeel)
    {
        StoredData.finiteDownload = finite;
        if (finite == true)
            StoredData.sameTimeDonwload = sameTimeDownload;
        StoredData.downloadPath = path;
        StoredData.lookAndFeelName = lookAndFeel;
    }

    /**
     * Changes the look and feel of the program
     */
    public static void changeLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(StoredData.lookAndFeels.get(StoredData.lookAndFeelName));
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Changes the order of downloads in the queue.
     * @param up move up or down
     */
    public static void move(boolean up)
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.QUEUES);
        DownloadFile temp;

        if (up)
        {
            for (int i = 0; i < files.size(); i++)
            {
                if (files.get(i).isSelected() && i != 0)
                {
                    temp = files.get(i - 1);
                    files.set(i - 1, files.get(i));
                    files.set(i, temp);
                }
            }
        }
        else
        {
            for (int i = files.size()-1; i >= 0; i--)
            {
                if (files.get(i).isSelected() && i != files.size()-1)
                {
                    temp = files.get(i + 1);
                    files.set(i + 1, files.get(i));
                    files.set(i, temp);
                }
            }
        }
    }

    /**
     * Adds another download file to the list.
     * @param file the download file
     * @return true if the adding was successful.
     */
    public static boolean add(DownloadFile file)
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).getPath().equalsIgnoreCase(file.getPath()) && files.get(i).getName().equalsIgnoreCase(file.getName()))
                return false;
        }
        StoredData.getFiles(Enums.TabType.DEFAULT).add(file);
        if (file.isQueued())
            StoredData.getFiles(Enums.TabType.QUEUES).add(file);
        GuiManager.reloadContentWrapper();
        return true;
    }

    /**
     * Removes the selected items from the list.
     */
    public static void remove()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).isSelected())
            {
                if (files.get(i).isQueued())
                    StoredData.getFiles(Enums.TabType.QUEUES).remove(files.get(i));
                files.remove(i);
                i--;
            }
        }
    }

    /**
     * Cancels the download of the selected items in the list.
     */
    public static void cancel()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).isSelected())
            {
                cancelDownload(files.get(i));
            }
        }
    }

    /**
     * Pauses the download of the selected items in the list.
     */
    public static void pause()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).isSelected())
            {
                pauseDownload(files.get(i));
            }
        }
    }

    /**
     * Resumes the download of the selected items in the list.
     */
    public static void resume()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).isSelected())
            {
                startDownload(files.get(i));
            }
        }
    }

    /**
     * Starts downloading a file.
     * @param file the file to be downloaded
     */
    public static void startDownload(DownloadFile file)
    {
        if (file.getStatus() == Enums.DownloadingStatus.DOWNLOADING) return;

        int counter = 0;
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        for (DownloadFile f : files)
            if (f.getStatus() == Enums.DownloadingStatus.DOWNLOADING) counter++;
        if (StoredData.finiteDownload == true && counter >= StoredData.sameTimeDonwload) return;

        file.setStartDate();
        file.setStatus(Enums.DownloadingStatus.DOWNLOADING);
        file.setProgressed(10000);
        endDownload(file);
    }

    /**
     * Ends downloading a file.
     * @param file the file to be ended.
     */
    private static void endDownload(DownloadFile file)
    {
        file.setStatus(Enums.DownloadingStatus.COMPLETED);
        file.setEndDate();
    }

    /**
     * Cancels downloading a file.
     * @param file the download to be cancelled
     */
    public static void cancelDownload(DownloadFile file)
    {
        file.setProgressed(0);
        file.resetDate();
        file.setStatus(Enums.DownloadingStatus.CANCELLED);
    }

    /**
     * Pauses downloading a file.
     * @param file the download to be paused
     */
    public static void pauseDownload(DownloadFile file)
    {
        file.setStatus(Enums.DownloadingStatus.STOPPED);
    }

    /**
     * This method looks for the selected items and adds them to queue or removes them from queue if they are already queued
     */
    public static void addRemoveQueue()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);
        ArrayList<DownloadFile> queued = StoredData.getFiles(Enums.TabType.QUEUES);

        for (int i = 0 ; i < files.size() ; i++)
        {
            if (files.get(i).isSelected())
            {
                files.get(i).setQueued(!files.get(i).isQueued());
                if (files.get(i).isQueued())
                    queued.add(files.get(i));
                else
                    queued.remove(files.get(i));
            }
        }
    }

    public static void startQueue()
    {
        ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.QUEUES);
        for (int i = 0 ; i < files.size() ; i++)
        {
            startDownload(files.get(i));
            GuiManager.reloadContentWrapper();
        }
    }
}
