package frontend;

import backend.*;
import javafx.scene.control.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class draws a the frame of the new downlaod
 *
 * @author Mohammad Mozafri
 */
public class NewDownload
{
    Actions actions;
    JFrame newDownloadFrame;
    JButton ok, cancel;
    JLabel urlImage, fileImage, sizeLabel;
    JTextField urlField, nameField, givenTimeField, minutesField;
    JRadioButton downloadNow, downloadLater, toQueue, givenTime;

    /**
     * This constructor initializes a new jframe and fills it with components the together represent the new download frame.
     */
    public NewDownload()
    {
        newDownloadFrame = new JFrame("New Download");
        newDownloadFrame.setLayout(null);
        newDownloadFrame.setSize(600, 320);
        newDownloadFrame.setLocationRelativeTo(null);
        JPanel contentPane = new JPanel(null);
        contentPane.setBackground(Color.WHITE);
        newDownloadFrame.setContentPane(contentPane);
        newDownloadFrame.setResizable(false);

        actions = new Actions();

        urlImage = new JLabel(new ImageIcon("C:\\chain.png"));
        urlImage.setLocation(20, 35);
        urlImage.setSize(40, 36);

        urlField = new JTextField();
        urlField.setLocation(70, 40);
        urlField.setSize(500, 30);
        urlField.addFocusListener(actions);

        fileImage = new JLabel(new ImageIcon("C:\\folder.png"));
        fileImage.setLocation(20, 75);
        fileImage.setSize(40, 36);

        nameField = new JTextField();
        nameField.setLocation(70, 80);
        nameField.setSize(500, 30);

        sizeLabel = new JLabel("2.34 GB");
        sizeLabel.setLocation(500, 110);
        sizeLabel.setSize(100, 30);
        sizeLabel.setFont(new Font("Arial", 1, 15));

        ok = new JButton("OK");
        ok.setName("OK");
        ok.setLocation(360, 240);
        ok.setSize(100, 30);
        ok.addActionListener(actions);

        cancel = new JButton("Cancel");
        cancel.setName("Cancel");
        cancel.setLocation(470, 240);
        cancel.setSize(100, 30);
        cancel.addActionListener(actions);

        minutesField = new JTextField();
        minutesField.setLocation(250, 158);
        minutesField.setSize(200, 30);
        minutesField.setEditable(false);

        givenTimeField = new JTextField();
        givenTimeField.setLocation(250, 192);
        givenTimeField.setSize(200, 30);
        givenTimeField.setEditable(false);

        createRadioButton();

        newDownloadFrame.add(urlImage);
        newDownloadFrame.add(urlField);
        newDownloadFrame.add(fileImage);
        newDownloadFrame.add(nameField);
        newDownloadFrame.add(givenTimeField);
        newDownloadFrame.add(minutesField);
        newDownloadFrame.add(sizeLabel);
        newDownloadFrame.add(ok);
        newDownloadFrame.add(downloadNow);
        newDownloadFrame.add(downloadLater);
        newDownloadFrame.add(givenTime);
        newDownloadFrame.add(toQueue);
        newDownloadFrame.add(cancel);
    }
    public void showFrame()
    {
        newDownloadFrame.setVisible(true);
    }

    /**
     * Creates a radion button group that determine what to do with the new downlaod file.
     */
    private void createRadioButton()
    {
        downloadNow = new JRadioButton("Start download now.");
        downloadNow.setLocation(20, 130);
        downloadNow.setSize(220, 30);
        downloadNow.setBackground(Color.WHITE);
        downloadNow.setSelected(true);

        downloadLater = new JRadioButton("Start download some time later.");
        downloadLater.setLocation(20, 160);
        downloadLater.setSize(220, 30);
        downloadLater.setBackground(Color.WHITE);
        downloadLater.setName("DownloadLater");
        downloadLater.addItemListener(actions);

        givenTime = new JRadioButton("Start at a given time.");
        givenTime.setLocation(20, 190);
        givenTime.setSize(220, 30);
        givenTime.setBackground(Color.WHITE);
        givenTime.setName("GivenTime");
        givenTime.addItemListener(actions);

        toQueue = new JRadioButton("Set to queue.");
        toQueue.setLocation(20, 220);
        toQueue.setSize(220, 30);
        toQueue.setBackground(Color.WHITE);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(downloadNow);
        btnGroup.add(downloadLater);
        btnGroup.add(toQueue);
        btnGroup.add(givenTime);
    }

    private class Actions implements ActionListener, FocusListener, ItemListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JComponent eventSource = (JComponent)e.getSource();
            DownloadFile file;

            if (eventSource.getName().equalsIgnoreCase("ok"))
            {
                file = new DownloadFile(urlField.getText(), nameField.getText(),
                        100, StoredData.downloadPath, Enums.DownloadingStatus.STOPPED, false);

                if (downloadNow.isSelected())
                    ActionCenter.startDownload(file);
                else if (downloadLater.isSelected())
                    file.scheduleDownload(minutesField.getText(), 2);
                else if (givenTime.isSelected())
                    file.scheduleDownload(givenTimeField.getText(), 1);
                else if (toQueue.isSelected())
                    file.setQueued(true);

                if (!ActionCenter.add(file))
                    JOptionPane.showMessageDialog(newDownloadFrame, "A file with this name already exists. :(",
                            "Already Found", JOptionPane.CLOSED_OPTION);
                newDownloadFrame.dispose();
                GuiManager.reloadFrame();
            }
            else if (eventSource.getName().equalsIgnoreCase("cancel"))
            {
                newDownloadFrame.dispose();
                GuiManager.reloadFrame();
            }
        }

        @Override
        public void focusGained(FocusEvent e)
        {

        }

        @Override
        public void focusLost(FocusEvent e)
        {
            urlField.setText(urlize(urlField.getText()));
            nameField.setText(getName(urlField.getText()));
        }


        private String getName(String url)
        {
            return url.substring(url.lastIndexOf('/') + 1, url.length());
        }

        private String urlize(String url)
        {
            String newUrl = "";
            if (!url.startsWith("http://"))
                newUrl += "http://";
            newUrl += url;
            if (newUrl.lastIndexOf('/') > newUrl.lastIndexOf('.'))
                newUrl += ".html";
            return newUrl;
        }

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            JRadioButton event = (JRadioButton)e.getSource();
            if (event.getName().equalsIgnoreCase("downloadlater"))
            {
                minutesField.setEditable(event.isSelected());
            }
            else if (event.getName().equalsIgnoreCase("giventime"))
            {
                givenTimeField.setEditable(event.isSelected());
            }
        }
    }
}
