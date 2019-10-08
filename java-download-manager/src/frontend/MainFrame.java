package frontend;

import Resources.NamesAndColors;
import backend.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class creates the UI of the main window of the program
 */
public class MainFrame
{
    //fields
    public static String[] sideBarButtonNames = new String[]{"Processing", "Completed", "Queues", "Default"};
    public static String[] toolBarButtonNames = new String[]{"New Download", "Pause", "Resume", "Cancel", "Remove", "Settings",
            "AddRemoveQueue", "Up", "Down"};
    public static String[] toolBarIcons = new String[]{"add.png", "resume.png", "stop.png", "cancel.png", "delete.png", "settings.png",
    "add-remove-queue.png"};
    public static String[] menuItemNames = new String[]{"New Download", "Pause", "Resume", "Cancel", "Remove", "Settings", "Exit", "About"};

    private JFrame mainFrame, helpFrame;

    //fields related to main frame
    private int mainFrameWidth = StoredData.mainFrameWidth, mainFrameHeight = StoredData.mainFrameHeight, sideBarWidth = 260;
    private Enums.TabType currentTab = Enums.TabType.DEFAULT;
    private JPanel contentWrapper, topPanel, toolBar, sideBar, downloadInfo;
    private JScrollPane scroller;
    private JMenuBar menuBar;
    private Actions events;

    /**
     * This constructor creates the main window of the program.
     */
    public MainFrame()
    {
        mainFrame = new JFrame("Eagle Download Manager");
        mainFrame.setSize(mainFrameWidth, mainFrameHeight);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setPreferredSize(new Dimension(mainFrameWidth - sideBarWidth, mainFrameHeight));
        events = new Actions();

        mainFrame.addComponentListener(events);
        mainFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                addToSystemTray();
            }
        });

        topPanel = new JPanel(new BorderLayout());

        createSideBar();
        createMenu();
        createToolBar();
        showTab(Enums.TabType.DEFAULT);

        topPanel.add(menuBar, BorderLayout.NORTH);

        contentWrapper.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Draws the UI of the menu inside the main frame.
     */
    private void createMenu()
    {
        JMenuItem mItem;
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(mainFrameWidth - sideBarWidth, 50));

        menuBar.add(new JMenu("Download"));
        menuBar.add(new JMenu("Help"));
        menuBar.getMenu(0).setMnemonic(KeyEvent.VK_D);
        menuBar.getMenu(1).setMnemonic(KeyEvent.VK_H);

        for (int i = 0; i < 7; i++)
        {
            mItem = new JMenuItem(menuItemNames[i]);
            mItem.setName(menuItemNames[i]);
            mItem.addActionListener(events);
            mItem.setMnemonic((int) menuItemNames[i].charAt(0));
            if (i == 4)
                mItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.SHIFT_MASK));
            else
                mItem.setAccelerator(KeyStroke.getKeyStroke((int) menuItemNames[i].charAt(0), KeyEvent.ALT_MASK));

            menuBar.getMenu(0).add(mItem);
        }

        mItem = new JMenuItem("About");
        mItem.setName("About");
        mItem.addActionListener(events);
        mItem.setMnemonic(KeyEvent.VK_A);
        mItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
        menuBar.getMenu(1).add(mItem);
    }

    /**
     * Creates the UI of the sidebar inside of the main frame.
     */
    public void createSideBar()
    {
        JButton button;
        JLabel logo = new JLabel(new ImageIcon("Icons/logo.png"));
        String[] iconNames = new String[] {"processing.png", "completed.png", "queues.png", ""};

        if (sideBar != null)
            mainFrame.remove(sideBar);

        sideBar = new JPanel(null);
        sideBar.setPreferredSize(new Dimension(sideBarWidth, mainFrameHeight));
        sideBar.setBackground(NamesAndColors.sideBar);

        logo.setLocation(10, 10);
        logo.setSize(240, 153);

        sideBar.add(logo);
        for (int i = 0; i < 4; i++)
        {
            button = CustomComponents.createButton(10, 170 + 50 * i, sideBarWidth - 20, 40, sideBarButtonNames[i],
                    "Icons/SideBarIcons/" + iconNames[i], NamesAndColors.sideBar, NamesAndColors.sideBarSelected);
            button.setText("  " + sideBarButtonNames[i]);
            button.setFont(new Font("Arial", Font.BOLD, 17));
            button.setHorizontalAlignment(2);
            button.addActionListener(events);
            button.setForeground(Color.WHITE);
            if (button.getName().equalsIgnoreCase(currentTab.toString()))
                button.setBackground(NamesAndColors.sideBarSelected);
            else
                button.setBackground(NamesAndColors.sideBar);
            sideBar.add(button);
        }

        mainFrame.add(sideBar, BorderLayout.WEST);
    }

    /**
     * Draws the UI of the toolbar inside the main frame.
     */
    public void createToolBar()
    {
        int height = 50, buttonWidth = 70, buttonHeight = 36;
        String[] toolTips = new String[] {"Add a new download", "Start or Resume downloading", "Pause downloading", "Cancel Downloading",
                "Delete a download", "Settings", "Add to/Remove from queue"};
        JButton button;

        if (toolBar != null)
        {
            topPanel.remove(toolBar);
        }


        toolBar = new JPanel(null);
        toolBar.setPreferredSize(new Dimension(mainFrameWidth - sideBarWidth, height));
        toolBar.setBackground(NamesAndColors.toolBar);

        for (int i = 0; i < 7; i++)
        {
            button = CustomComponents.createButton(20 + buttonWidth * i, 7, buttonWidth, buttonHeight, toolBarButtonNames[i],
                    "Icons/" + toolBarIcons[i], NamesAndColors.toolBar, NamesAndColors.toolBarPressed);
            button.addActionListener(events);
            button.setToolTipText(toolTips[i]);
            toolBar.add(button);
        }

        button = CustomComponents.createButton(510, 7, buttonWidth, buttonHeight, toolBarButtonNames[7],
                "Icons/up(active).png", NamesAndColors.toolBar, NamesAndColors.toolBarPressed);
        button.setEnabled(currentTab == Enums.TabType.QUEUES && StoredData.isAnyQueudFileSelected());
        button.addActionListener(events);
        button.setToolTipText("Move Higher");
        toolBar.add(button);

        button = CustomComponents.createButton(580, 7, buttonWidth, buttonHeight, toolBarButtonNames[8],
                "Icons/down(active).png", NamesAndColors.toolBar, NamesAndColors.toolBarPressed);
        button.setEnabled(currentTab == Enums.TabType.QUEUES && StoredData.isAnyQueudFileSelected());
        button.addActionListener(events);
        button.setToolTipText("Move Lower");
        toolBar.add(button);

        button = CustomComponents.createButton(650, 7, buttonWidth, buttonHeight, "StartQueue", "Icons/start-queue.png",
                NamesAndColors.toolBar, NamesAndColors.toolBarPressed);
        button.setEnabled(currentTab == Enums.TabType.QUEUES);
        button.addActionListener(events);
        button.setToolTipText("Start Queue");
        toolBar.add(button);

        topPanel.add(toolBar);
    }

    /**
     * This method shows list of the downloads and the download info.
     * @param tabType the type of the tab to be displayed
     */
    public void showTab(Enums.TabType tabType)
    {
        int width = mainFrameWidth - sideBarWidth, height = mainFrameHeight - 100;
        ArrayList<DownloadFile> files;
        JPanel downloadList;

        currentTab = tabType;
        if (scroller != null)
            contentWrapper.remove(scroller);
        if (downloadInfo != null)
        {
            contentWrapper.remove(downloadInfo);
            downloadInfo = null;
        }
        files = StoredData.getFiles(tabType);

        for (int i = 0 ; i < files.size() ; i++)
            if (files.get(i).isInfoShown())
                width = mainFrameWidth - 2 * sideBarWidth;

        if (files.size() > (height) / 100)
            downloadList = new JPanel(new GridLayout(files.size(), 1));
        else
            downloadList = new JPanel(new GridLayout((height) / 100, 1));

        downloadList.setBackground(new Color(231, 239, 251));
        for (int i = 0 ; i < files.size() ; i++)
        {
            GuiManager.downloadList.put(files.get(i), createDownloadFilePanel(files.get(i), width, 100));
            downloadList.add(GuiManager.downloadList.get(files.get(i)));
            if (files.get(i).isInfoShown())
                createInfoPanel(files.get(i), sideBarWidth * 4 / 3, height);
        }
        scroller = new JScrollPane(downloadList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setLocation(50, 20);
        scroller.setBorder(null);

        contentWrapper.add(scroller, BorderLayout.CENTER);
        if (downloadInfo != null)
            contentWrapper.add(downloadInfo, BorderLayout.EAST);
        contentWrapper.setVisible(false);
        contentWrapper.setVisible(true);
    }

    /**
     * Creates a panel that displays status of a download file.
     * @param file the download file
     * @param width width of the panel
     * @param height height of the panel
     * @return a panel displaying the progress of download
     */
    private JPanel createDownloadFilePanel(DownloadFile file, int width, int height)
    {
        JPanel newDownloadFilePanel = new JPanel();
        JLabel progressPercentage;
        JLabel name, size, image;
        JButton openFolder, resume, cancel, pause;
        JProgressBar progress;

        Font font = new Font("Cambria", 1, 15);
        String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1, file.getName().length());

        newDownloadFilePanel.setPreferredSize(new Dimension(width, height));
        newDownloadFilePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, NamesAndColors.lineColor));
        newDownloadFilePanel.setLayout(null);
        newDownloadFilePanel.setLocation(0, 0);
        newDownloadFilePanel.setBackground(NamesAndColors.downloadFile);
        newDownloadFilePanel.setName("DownloadPanel:" + file.getName());
        newDownloadFilePanel.addMouseListener(events);

        if (file.isSelected())
            newDownloadFilePanel.setBackground(NamesAndColors.selectedDownload);

        image = new JLabel();
        if (extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("mp4") ||
                extension.equalsIgnoreCase("avi"))
            image.setIcon(new ImageIcon("Icons/FileTypes/video.png"));
        else if (extension.equalsIgnoreCase("mp3"))
            image.setIcon(new ImageIcon("Icons/FileTypes/music.png"));
        else if (extension.equalsIgnoreCase("pdf"))
            image.setIcon(new ImageIcon("Icons/FileTypes/pdf.png"));
        else if (extension.equalsIgnoreCase("exe"))
            image.setIcon(new ImageIcon("Icons/FileTypes/exe.png"));
        else
            image.setIcon(new ImageIcon("Icons/FileTypes/default.png"));
        image.setLocation(30, (height - 70) / 2);
        image.setSize(70, 70);

        progress = new JProgressBar(0, 0, 10000);
        progress.setValue(file.getProgressed());
        progress.setLocation(120, 30);
        progress.setSize(width - 320, 20);

        double downloaded = progress.getValue() / progress.getMaximum() * file.getSize();
        name = CustomComponents.createLabel(120, 5, width - 320, 25, file.getName(), font, null);
        size = CustomComponents.createLabel(width - 350, 50, 300, 25,
                "9.32 MB/sec (" + downloaded + "/" + file.getSize() + ")", font, null);

        progressPercentage = CustomComponents.createLabel(width - 190, 30, 80, 20,
                progress.getValue() * 100 / progress.getMaximum() + "%", new Font("Arial", Font.BOLD, 18), null);

        openFolder = CustomComponents.createButton(width - 100, 30, 40, 40, "-",
                "Icons/DownloadPanelButtons/open-folder.png", null, null);
        openFolder.addActionListener(e ->
        {
            FileHandler.openPath(file.getPath());
        });

        resume = CustomComponents.createButton(120, 50, 25, 24, "-",
                "Icons/DownloadPanelButtons/play.png", null, null);
        resume.addActionListener(e ->
        {
            ActionCenter.startDownload(file);
            GuiManager.reloadContentWrapper();
        });

        pause = CustomComponents.createButton(150, 50, 25, 25, "-",
                "Icons/DownloadPanelButtons/pause.png", null, null);
        pause.addActionListener(e ->
        {
            ActionCenter.pauseDownload(file);
            GuiManager.reloadContentWrapper();
        });

        cancel = CustomComponents.createButton(180, 50, 25, 25, "-",
                "Icons/DownloadPanelButtons/cancel.png", null, null);
        cancel.addActionListener(e ->
        {
            ActionCenter.cancelDownload(file);
            GuiManager.reloadContentWrapper();
        });

        newDownloadFilePanel.add(image);
        newDownloadFilePanel.add(name);
        newDownloadFilePanel.add(progressPercentage);
        newDownloadFilePanel.add(progress);
        newDownloadFilePanel.add(openFolder);
        newDownloadFilePanel.add(size);
        newDownloadFilePanel.add(resume);
        newDownloadFilePanel.add(pause);
        newDownloadFilePanel.add(cancel);
        return newDownloadFilePanel;
    }

    /**
     * This panel is shown when right clicked on a download file panel and displays download info.
     * @param file the file info to be shown
     * @param width the preferred width
     * @param height the preferred height
     */
    private void createInfoPanel(DownloadFile file, int width, int height)
    {
        Font mainFont = new Font("Arial", 1, 17);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDownload = file.getStartDate() == null ? "-" : dateFormat.format(file.getStartDate());
        String endDownload = file.getEndDate() == null ? "-" : dateFormat.format(file.getEndDate());
        String status = file.getStatus().toString();
        status = status.substring(0, 1).concat(status.substring(1, status.length()).toLowerCase());
        JLabel data;

        downloadInfo = new JPanel(new GridLayout(12, 2));
        downloadInfo.setBackground(Color.WHITE);
        downloadInfo.setFont(new Font("Arial", 1, 17));
        downloadInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        downloadInfo.setForeground(NamesAndColors.mainFontColor);

        data = CustomComponents.createLabel(0, 0, 0, 0, file.getName(), mainFont, NamesAndColors.mainFontColor);
        data.setToolTipText(file.getName());
        downloadInfo.add(new Label("File:"));
        downloadInfo.add(data);

        downloadInfo.add(new Label("Status:"));
        downloadInfo.add(new Label(status));
        downloadInfo.add(new Label("Size:"));
        downloadInfo.add(new Label(file.getSize() + ""));

        data = CustomComponents.createLabel(0, 0, 0, 0, file.getPath(), mainFont, NamesAndColors.mainFontColor);
        data.setToolTipText(file.getPath());
        downloadInfo.add(new Label("Save To:"));
        downloadInfo.add(data);

        downloadInfo.add(new Label("Progress:"));
        downloadInfo.add(new Label("---"));

        downloadInfo.add(new Label("Created:"));
        downloadInfo.add(new Label(startDownload));
        downloadInfo.add(new Label("Finished:"));
        downloadInfo.add(new Label(endDownload));

        data = CustomComponents.createLabel(0, 0, 0, 0, file.getUrl(), mainFont, NamesAndColors.mainFontColor);
        data.setToolTipText(file.getUrl());
        downloadInfo.add(new Label("URL:"));
        downloadInfo.add(data);

        downloadInfo.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Displayes the about frame.
     */
    private void showHelpFrame()
    {
        Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
        String message = "Program Info\nName : Mohammad Mozafari\nStudent Number : 9631069\nStart project at : April 25, 2018\n" +
                "End project at : May 18, 2018\n\n" + "----------------------\n\n A Guide Through\n";
        String howToUse = "The program consists of 4 frames : MainFrame, Settigs, NewDownload, Help.\n" +
                "MainFrame : Displays the status of downloads and allows opening other frames.\n" +
                "Settings : You can change the program settings here such as save path, look and feel and etc.\n" +
                "NewDownload : You can add new downloads from here by settings url and you can choose whether to download immediately or later" +
                " you can also add it to queue.\n" +
                "Help : You are visiting it right now. :)";

        helpFrame = new JFrame("Help Window");
        helpFrame.setResizable(false);
        helpFrame.setLayout(null);
        helpFrame.setLocation((dm.width - 400) / 2, (dm.height - 500) / 2);
        helpFrame.setSize(400, 550);

        JTextArea txtArea = new JTextArea(message + howToUse);
        txtArea.setFont(new Font("Cambria", Font.PLAIN, 18));
        txtArea.setBackground(new Color(201, 255, 180));
        txtArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtArea.setLineWrap(true);
        txtArea.setEditable(false);
        txtArea.setLocation(0, 0);
        txtArea.setSize(400, 550);

        helpFrame.add(txtArea);
        helpFrame.setVisible(true);
    }

    /**
     * This method adds the icon program icon to the System Tray and brings it back when double clicked on the icon.
     */
    private void addToSystemTray()
    {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("Icons/download.png"));
        icon.addActionListener(e ->
        {
            showFrame();
            SystemTray.getSystemTray().remove((TrayIcon)e.getSource());
        });
        try
        {
            tray.add(icon);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    public void dispose()
    {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }

    public void showFrame()
    {
        mainFrame.setVisible(true);
    }

    public Enums.TabType getCurrentTab()
    {
        return currentTab;
    }

    /**
     * This private class is responsible for handling all the events envoked from the main frame.
     *
     * @author Mohammad Mozafari
     */
    private class Actions extends ComponentAdapter implements ActionListener, MouseListener
    {

        /**
         * Handles the events that invoke actionPerformed event.
         * @param e the info of the source of the event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JComponent eventSource = (JComponent)e.getSource();
            if (eventSource.getName().equalsIgnoreCase(MainFrame.sideBarButtonNames[0]))
            {
                showTab(Enums.TabType.PROCESSING);
                GuiManager.reloadFrame();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.sideBarButtonNames[1]))
            {
                showTab(Enums.TabType.COMPLETED);
                GuiManager.reloadFrame();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.sideBarButtonNames[2]))
            {
                showTab(Enums.TabType.QUEUES);
                GuiManager.reloadFrame();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.sideBarButtonNames[3]))
            {
                showTab(Enums.TabType.DEFAULT);
                GuiManager.reloadFrame();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[0]))
                showNewDownload();
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[1]))
            {
                ActionCenter.resume();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[2]))
            {
                ActionCenter.pause();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[3]))
            {
                ActionCenter.cancel();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[4]))
            {
                ActionCenter.remove();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[5]))
                showSettings();
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[6]))
            {
                ActionCenter.addRemoveQueue();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[7]))
            {
                ActionCenter.move(true);
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.toolBarButtonNames[8]))
            {
                ActionCenter.move(false);
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase("startqueue"))
            {
                ActionCenter.startQueue();
                GuiManager.reloadContentWrapper();
            }
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.menuItemNames[6]))
                System.exit(1);
            else if (eventSource.getName().equalsIgnoreCase(MainFrame.menuItemNames[7]))
                showHelpFrame();
        }

        //MouseListener events
        @Override
        public void mouseClicked(MouseEvent e)
        {
            Component source = (Component)e.getSource();
            ArrayList<DownloadFile> files = StoredData.getFiles(Enums.TabType.DEFAULT);

            if (source instanceof JPanel && e.getButton() != MouseEvent.BUTTON2)
            {
                for (int i = 0; i < files.size(); i++)
                {
                    if (source.getName().equalsIgnoreCase("DownloadPanel:" + files.get(i).getName()))
                    {
                        if (e.getClickCount() == 2)
                        {
                            try
                            {
                                FileHandler.openPath(files.get(i).getPath() + "\\" + files.get(i).getName());
                            }
                            catch (IllegalArgumentException exception)
                            {
                                JOptionPane.showMessageDialog(mainFrame,"This file no longer exists");
                            }
                        }
                        if (e.getButton() == MouseEvent.BUTTON3)
                        {
                            files.get(i).setInfoShown(!files.get(i).isInfoShown());
                            files.get(i).setSelected(files.get(i).isInfoShown());
                        }
                        else if (e.getButton() == MouseEvent.BUTTON1)
                        {
                            if (files.get(i).isInfoShown())
                            {
                                files.get(i).setInfoShown(false);
                                files.get(i).setSelected(false);
                            }
                            else
                            {
                                files.get(i).setSelected(!files.get(i).isSelected());
                            }
                        }
                        GuiManager.reloadContentWrapper();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {

        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }

        //ComponentAdapter method overriden
        @Override
        public void componentResized(ComponentEvent e)
        {
            mainFrameWidth = mainFrame.getSize().width;
            mainFrameHeight = mainFrame.getSize().height;
            StoredData.changeMainFrameDimensions(mainFrameWidth, mainFrameHeight);
            GuiManager.reloadFrame();
        }

        //Private Methods
        private void showSettings()
        {
            GuiManager.settingsForm = new SettingsForm();
            GuiManager.settingsForm.showFrame();
        }

        private void showNewDownload()
        {
            GuiManager.newDownload = new NewDownload();
            GuiManager.newDownload.showFrame();
        }
    }
}
