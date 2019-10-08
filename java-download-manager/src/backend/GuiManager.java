package backend;

import frontend.MainFrame;
import frontend.NewDownload;
import frontend.SettingsForm;

import javax.swing.*;
import java.util.HashMap;

/**
 * This class has methods used for managing the UI.
 *
 * @author Mohammad Mozafari
 */
public class GuiManager
{

    public static MainFrame mainFrame;
    public static NewDownload newDownload;
    public static SettingsForm settingsForm;
    public static HashMap<DownloadFile, JPanel> downloadList = new HashMap<>();

    /**
     * Closes and opens the main frame to reload and display the changes.
     */
    public static void loadMainFrame()
    {
        if (mainFrame != null)
        {
            mainFrame.dispose();
        }
        mainFrame = new MainFrame();
        mainFrame.showFrame();
    }

    /**
     * Reloads the main parts of the main frame.
     */
    public static void reloadFrame()
    {
        mainFrame.showTab(mainFrame.getCurrentTab());
        mainFrame.createSideBar();
        mainFrame.createToolBar();
    }

    /**
     * Reloads the content wrapper
     */
    public static void reloadContentWrapper()
    {
        mainFrame.showTab(mainFrame.getCurrentTab());
    }
}
