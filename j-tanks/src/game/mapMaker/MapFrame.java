package game.mapMaker;

import game.mapMaker.InGameMap.MapConstructor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * this class is a frame which allows the player to make their own map
 *
 * @auther seyyed soroush mortazavi moghaddam
 */
public class MapFrame extends JFrame
{
    //different buttons to choose and paint with
    private JButton unDestroyableWall1Btn;
    private JButton unDestroyableWall2Btn;
    private JButton destroyableWallBtn;
    private JButton movingEnemy1Btn;
    private JButton movingEnemy2Btn;
    private JButton movelessEnemy1Btn;
    private JButton movelessEnemy2Btn;
    private JButton tankPositionBtn;
    private JButton deleteBtn;
    private JButton treeBtn;
    private JButton tankRepairKit;
    private JButton cannonShells;
    private JButton machineGunCartridges;
    private JButton bounce;
    private JButton endingPosition;
    private JMenuItem newMap;
    private JMenuItem saveMap;
    private JMenuItem loadMap;
    private JMenuItem showMap;
    private DrawPanel drawPanel;
    private MapConstructor mapConstructor;
    private int scale;
    private boolean stop = true;
    //pressedButton is to hold an string to know which button is clicked;
    private String pressedButton;

    //these are the equivalence of each string :
    //udw1 : unDestroyableWall1Btn
    //udw2 : unDestroyableWall2Btn
    //dw : destroyableWallBtn
    //me1 : movingEnemy1Btn
    //me2 : movingEnemy2Btn
    //mle1 : movelessEnemy1Btn
    //mle2 : movelessEnemy2Btn
    //tp : tankPositionBtn
    //d : deleteBtn
    //t : treeBtn
    //trk : tankRepairKit
    //cs : cannonShells
    //mgc : machineGunCartridges

    public MapFrame(int scaleH) throws HeadlessException
    {
        setTitle("Map Maker");
        setLocation(200, 50);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        scale = scaleH;
        //creating the the painting panel
        drawPanel = new DrawPanel(scaleH);

        //adding the menu bar
        JMenuBar menubar = new JMenuBar();
        JMenu mainMenu = new JMenu("Menu");
        newMap = new JMenuItem("New Map");
        saveMap = new JMenuItem("save");
        loadMap = new JMenuItem("load map");
        showMap = new JMenuItem("show map");
        menubar.add(mainMenu);
        mainMenu.add(newMap);
        mainMenu.add(saveMap);
        mainMenu.add(loadMap);
        mainMenu.add(showMap);

        //creating the buttons
        unDestroyableWall1Btn = new JButton("Strong wall 1",new ImageIcon("src/images/Icons/strongWall1Icon.png"));
        unDestroyableWall2Btn = new JButton("Strong wall 2",new ImageIcon("src/images/Icons/strongWall2Icon.png"));
        destroyableWallBtn = new JButton("weak wall",new ImageIcon("src/images/Icons/weakWallIcon.png"));
        movingEnemy1Btn = new JButton("moving enemy 1",new ImageIcon("src/images/Icons/movingEnemy1Icon.png"));
        movingEnemy2Btn = new JButton("moving enemy 2",new ImageIcon("src/images/Icons/movingEnemy2Icon.png"));
        movelessEnemy1Btn = new JButton("moveless enemy 1",new ImageIcon("src/images/Icons/movelessEnemy1Icon.png"));
        movelessEnemy2Btn = new JButton("moveless enemy 2",new ImageIcon("src/images/Icons/movelessEnemy2Icon.png"));
        tankPositionBtn = new JButton("tank starting position",new ImageIcon("src/images/Icons/tankStartingPositionIcon.png"));
        deleteBtn = new JButton("remove",new ImageIcon("src/images/Icons/eraser.png"));
        treeBtn = new JButton("trees",new ImageIcon("src/images/Icons/treeIcon.png"));
        tankRepairKit = new JButton("repair Kit",new ImageIcon("src/images/Icons/repairKitIcon.png"));
        cannonShells = new JButton("cannon shells",new ImageIcon("src/images/Icons/cannonShellsIcon.png"));
        machineGunCartridges = new JButton("machine Gun Cartridges",new ImageIcon("src/images/Icons/machineGunCartridgesIcon.png"));
        bounce = new JButton("bounce",new ImageIcon("src/images/Icons/bounceIcon.png"));
        endingPosition = new JButton("ending position",new ImageIcon("src/images/Icons/endingPositionIcon.png"));

        //creating a panel for holding the creator buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(15, 1, 3, 5));
        buttonsPanel.setBorder(new EmptyBorder(2, 5, 2, 5));

        //adding the buttons to the panel
        buttonsPanel.add(unDestroyableWall1Btn);
        buttonsPanel.add(unDestroyableWall2Btn);
        buttonsPanel.add(destroyableWallBtn);
        buttonsPanel.add(movingEnemy1Btn);
        buttonsPanel.add(movingEnemy2Btn);
        buttonsPanel.add(movelessEnemy1Btn);
        buttonsPanel.add(movelessEnemy2Btn);
        buttonsPanel.add(tankPositionBtn);
        buttonsPanel.add(treeBtn);
        buttonsPanel.add(tankRepairKit);
        buttonsPanel.add(cannonShells);
        buttonsPanel.add(machineGunCartridges);
        buttonsPanel.add(bounce);
        buttonsPanel.add(endingPosition);
        buttonsPanel.add(deleteBtn);

        keyListener keyListener = new keyListener();
        unDestroyableWall1Btn.addActionListener(keyListener);
        unDestroyableWall2Btn.addActionListener(keyListener);
        destroyableWallBtn.addActionListener(keyListener);
        movingEnemy1Btn.addActionListener(keyListener);
        movingEnemy2Btn.addActionListener(keyListener);
        movelessEnemy1Btn.addActionListener(keyListener);
        movelessEnemy2Btn.addActionListener(keyListener);
        tankPositionBtn.addActionListener(keyListener);
        deleteBtn.addActionListener(keyListener);
        treeBtn.addActionListener(keyListener);
        tankRepairKit.addActionListener(keyListener);
        cannonShells.addActionListener(keyListener);
        machineGunCartridges.addActionListener(keyListener);
        bounce.addActionListener(keyListener);
        endingPosition.addActionListener(keyListener);

        MenuItemListener menuItemListener = new MenuItemListener();
        newMap.addActionListener(menuItemListener);
        saveMap.addActionListener(menuItemListener);
        loadMap.addActionListener(menuItemListener);
        showMap.addActionListener(menuItemListener);

        add(menubar, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.WEST);
        add(drawPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private class keyListener implements ActionListener
    {
        //the meaning of each string:
        //udw1 : unDestroyableWall1Btn
        //udw2 : unDestroyableWall2Btn
        //dw : destroyableWallBtn
        //me1 : movingEnemy1Btn
        //me2 : movingEnemy2Btn
        //mle1 : movelessEnemy1Btn
        //mle2 : movelessEnemy2Btn
        //tp : tankPositionBtn
        //d : deleteBtn
        //t : treeBtn
        //trk : tankRepairKit
        //cs : cannonShells
        //mgc : machineGunCartridges
        //b : bounce
        //eP : endingPosition

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource().equals(unDestroyableWall1Btn))
                pressedButton = "udw1";
            else if (e.getSource().equals(unDestroyableWall2Btn))
                pressedButton = "udw2";
            else if (e.getSource().equals(destroyableWallBtn))
                pressedButton = "dw";
            else if (e.getSource().equals(movingEnemy1Btn))
                pressedButton = "me1";
            else if (e.getSource().equals(movingEnemy2Btn))
                pressedButton = "me2";
            else if (e.getSource().equals(movelessEnemy1Btn))
                pressedButton = "mle1";
            else if (e.getSource().equals(movelessEnemy2Btn))
                pressedButton = "mle2";
            else if (e.getSource().equals(tankPositionBtn))
                pressedButton = "tp";
            else if (e.getSource().equals(deleteBtn))
                pressedButton = "d";
            else if (e.getSource().equals(treeBtn))
                pressedButton = "t";
            else if (e.getSource().equals(tankRepairKit))
                pressedButton = "trk";
            else if (e.getSource().equals(cannonShells))
                pressedButton = "cs";
            else if (e.getSource().equals(machineGunCartridges))
                pressedButton = "mgc";
            else if (e.getSource().equals(bounce))
                pressedButton = "b";
            else if (e.getSource().equals(endingPosition))
                pressedButton = "eP";
            drawPanel.setBrush(pressedButton);
        }
    }

    private class MenuItemListener implements ActionListener
    {

        //handling event done on the menus
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource().equals(newMap))
            {
                String newScale = JOptionPane.showInputDialog(null, "enter the scale of the map", "Enter digits");
                try
                {
                    scale = Integer.parseInt(newScale);
                } catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(null, "you didn't enter a correct number (30 was taken instead)");
                    scale = 30;
                }

                stop = true;
                remove(drawPanel);
                if (mapConstructor != null)
                    remove(mapConstructor);
                drawPanel = new DrawPanel(scale);
                add(drawPanel, BorderLayout.CENTER);
                drawPanel.setBrush(pressedButton);
                drawPanel.setVisible(false);
                drawPanel.setVisible(true);

            } else if (e.getSource().equals(saveMap))
            {
                FileWriter fileWriter = null;
                try
                {
                    fileWriter = new FileWriter("src/maps/map1.map");
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                PrintWriter printWriter = new PrintWriter(fileWriter);
                for (int k = 0; k < scale; k++)
                {
                    for (int p = 0; p < scale; p++)
                    {
                        printWriter.print(drawPanel.getCells()[k][p] + ",");
                    }
                    printWriter.println();
                }
                printWriter.close();
                JOptionPane.showMessageDialog(drawPanel, "map successfully saved", "map saved", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource().equals(loadMap))
            {
                JFileChooser jfc = new JFileChooser("src/maps");

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    if (mapConstructor != null)
                        remove(mapConstructor);
                    add(drawPanel);
                    File selectedFile = jfc.getSelectedFile();
                    FileReader fileReader = null;
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile)))
                    {
                        String line = br.readLine();
                        scale = line.split(",").length;
                    }
                    catch (IOException ex)
                    {
                        JOptionPane.showMessageDialog(null,"can't Open map","error",JOptionPane.ERROR_MESSAGE);
                    }
                    try
                    {
                        fileReader = new FileReader(selectedFile);
                    } catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }
                    assert fileReader != null;
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    int[][] cells = new int[scale][scale];
                    String line;
                    int i = 0;
                    try
                    {
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            for (int j = 0; j < scale; j++)
                            {
                                cells[i][j] = Integer.parseInt(line.split(",")[j]);
                            }
                            i++;
                        }
                        fileReader.close();
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    drawPanel.setCells(cells, cells[0].length);
                }
            } else if (e.getSource().equals(showMap))
            {
                mapConstructor = new MapConstructor(drawPanel.getCells(), scale, 24);
                remove(drawPanel);
                add(mapConstructor, BorderLayout.CENTER);
                setVisible(false);
                setVisible(true);
            }
        }
    }

}
