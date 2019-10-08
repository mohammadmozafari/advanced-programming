package frontend;

import backend.ActionCenter;
import backend.GuiManager;
import backend.StoredData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * The UI of the settings window.
 *
 * @author Mohammad Mozafari
 */
public class SettingsForm
{
    //fields
    Actions actions;
    JButton selectPath, saveButton;
    JComboBox<String> lookFeel;
    ButtonGroup buttonGroup;
    JRadioButton infinite, finite;
    JFileChooser fileChooser;
    JFrame settings;
    JLabel sameTimeLabel;
    JSpinner sameTime;
    JTextField path;
    String[] lookAndFeels;

    /**
     * Draws the UI of the settings window.
     */
    public SettingsForm()
    {
        settings = new JFrame("Settings");
        settings.setLayout(null);
        settings.setSize(500, 320);
        settings.setLocationRelativeTo(null);
        settings.setResizable(false);

        actions = new Actions();

        createRadioButton();
        sameTime = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        sameTime.setEnabled(finite.isSelected());
        sameTime.setValue(StoredData.sameTimeDonwload);
        sameTime.setLocation(200, 40);
        sameTime.setSize(50, 40);

        sameTimeLabel = new JLabel("Download(s) at the same time.");
        sameTimeLabel.setLocation(260, 40);
        sameTimeLabel.setSize(190, 40);

        path = new JTextField(StoredData.downloadPath);
        path.setEditable(false);
        path.setLocation(50, 120);
        path.setSize(300, 40);

        selectPath = new JButton("Path...");
        selectPath.setName("Path");
        selectPath.setLocation(360, 120);
        selectPath.setSize(90, 40);
        selectPath.addActionListener(actions);

        String choosertitle = "Select a directory";
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(StoredData.downloadPath));
        fileChooser.setDialogTitle(choosertitle);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        lookAndFeels = new String[] {"Nimbus", "Metal", "System Default"};
        lookFeel = new JComboBox<>(lookAndFeels);
        lookFeel.setSelectedItem(StoredData.lookAndFeelName);
        lookFeel.setLocation(50, 200);
        lookFeel.setSize(200, 40);

        saveButton = new JButton("Save Changes");
        saveButton.setName("Save");
        saveButton.setLocation(300, 200);
        saveButton.setSize(150, 40);
        saveButton.addActionListener(actions);

        settings.add(infinite);
        settings.add(finite);
        settings.add(sameTime);
        settings.add(sameTimeLabel);
        settings.add(path);
        settings.add(selectPath);
        settings.add(lookFeel);
        settings.add(saveButton);
    }

    public void showFrame()
    {
        settings.setVisible(true);
    }

    /**
     * This method adds the radio buttons to the settings frame.
     */
    private void createRadioButton()
    {
        infinite = new JRadioButton("Infinite");
        finite = new JRadioButton("Finite");

        infinite.setLocation(50, 40);
        infinite.setName("Infinite");
        infinite.setSize(150, 20);
        infinite.addActionListener(actions);

        finite.setLocation(50, 60);
        finite.setName("Finite");
        finite.setSize(150, 20);
        finite.addActionListener(actions);

        if (StoredData.finiteDownload)
        {
            infinite.setSelected(false);
            finite.setSelected(true);
        }
        else
        {
            infinite.setSelected(true);
            finite.setSelected(false);
        }

        buttonGroup = new ButtonGroup();
        buttonGroup.add(infinite);
        buttonGroup.add(finite);
    }

    /**
     * Handles the events invoked by any component inside the settings frame
     */
    private class Actions extends JPanel implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JComponent eventSource = (JComponent)e.getSource();

            if (eventSource.getName().equals("Path"))
            {
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                {
                    path.setText(fileChooser.getSelectedFile().toString());
                }
            }
            else if (eventSource.getName().equals("Save"))
            {
                if (!lookFeel.getSelectedItem().toString().equals(StoredData.lookAndFeelName))
                    StoredData.repaintNeeded = true;
                ActionCenter.saveSettings(path.getText(), finite.isSelected(), (int)sameTime.getValue(), lookFeel.getSelectedItem().toString());
                settings.dispose();
                if (StoredData.repaintNeeded)
                {
                    ActionCenter.changeLookAndFeel();
                    GuiManager.loadMainFrame();
                }
            }
            else if (eventSource.getName().equals("Infinite")) sameTime.setEnabled(false);
            else if (eventSource.getName().equals("Finite")) sameTime.setEnabled(true);
        }
    }
}
