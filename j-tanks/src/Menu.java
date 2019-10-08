import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * this class is the first frame to be shown for the player .
 * having the options to choose from
 */
public class Menu extends JFrame
{
    private JButton newGame, resume, host, connect, easy, medium, hard;
    private Actions actions = new Actions(this);

    /**
     * Draws the menu at the beginning of the game.
     */
    public Menu()
    {
        setLocation(300, 200);
        setSize(Info.mainFrameWidth, Info.mainFrameHeight);
        setLayout(null);
        setVisible(true);

        newGame = new JButton("New Game");
        newGame.setContentAreaFilled(true);
        resume = new JButton("Resume");
        host = new JButton("Become a host");
        connect = new JButton("Connect to others");
        easy = new JButton("Easy");
        easy.setPreferredSize(new Dimension(100, 20));
        medium = new JButton("Medium");
        medium.setPreferredSize(new Dimension(100, 20));
        hard = new JButton("Hard");
        hard.setPreferredSize(new Dimension(100, 20));

        newGame.setLocation(50, 50);
        newGame.setSize(200, 50);
        newGame.setContentAreaFilled(false);
        newGame.setOpaque(true);
        newGame.setBackground(Color.darkGray);
        newGame.setForeground(Color.white);

        resume.setLocation(50, 120);
        resume.setSize(200, 50);
        resume.setContentAreaFilled(false);
        resume.setOpaque(true);
        resume.setBackground(Color.darkGray);
        resume.setForeground(Color.white);

        host.setLocation(50, 190);
        host.setSize(200, 50);
        host.setContentAreaFilled(false);
        host.setOpaque(true);
        host.setBackground(Color.darkGray);
        host.setForeground(Color.white);

        connect.setLocation(50, 260);
        connect.setSize(200, 50);
        connect.setContentAreaFilled(false);
        connect.setOpaque(true);
        connect.setBackground(Color.darkGray);
        connect.setForeground(Color.white);

        easy.setLocation(50, 50);
        easy.setSize(200, 50);
        easy.setContentAreaFilled(false);
        easy.setOpaque(true);
        easy.setBackground(Color.darkGray);
        easy.setForeground(Color.white);
        easy.setFocusPainted(false);

        medium.setLocation(50, 120);
        medium.setSize(200, 50);
        medium.setContentAreaFilled(false);
        medium.setOpaque(true);
        medium.setBackground(Color.darkGray);
        medium.setForeground(Color.white);

        hard.setLocation(50, 190);
        hard.setSize(200, 50);
        hard.setContentAreaFilled(false);
        hard.setOpaque(true);
        hard.setBackground(Color.darkGray);
        hard.setForeground(Color.white);

        newGame.addActionListener(actions);
        resume.addActionListener(actions);
        host.addActionListener(actions);
        connect.addActionListener(actions);
        easy.addActionListener(actions);
        medium.addActionListener(actions);
        hard.addActionListener(actions);

        add(newGame);
        add(resume);
        add(host);
        add(connect);
        newGame.repaint();

        SoundMaker.play("src/Sounds/mainPage.wav");
    }

    private class Actions implements ActionListener
    {
        JFrame frame;

        public Actions(JFrame frame)
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource().equals(newGame))
            {
                frame.remove(newGame);
                frame.remove(resume);
                frame.remove(host);
                frame.remove(connect);
                frame.add(easy);
                frame.add(medium);
                frame.add(hard);
                frame.setVisible(false);
                frame.setVisible(true);
            }
            if (e.getSource().equals(easy))
            {
                Info.Quest=11;
                Info.mapLocation=("src/maps/map"+Info.Quest+".map");
              //  SoundMaker.inGameSound.stop();
              //  SoundMaker.inGameSound.close();
                Info.movingEnemy1 = 15;
                Info.movingEnemy2 = 20;
                Info.movelessEnemy1 = 20;
                Info.movelessEnemy2 = 25;
                dispose();
                Main.startGame();
            }
            if (e.getSource().equals(medium))
            {
                Info.Quest=21;
                Info.mapLocation=("src/maps/map"+Info.Quest+".map");
               // SoundMaker.inGameSound.stop();
              //  SoundMaker.inGameSound.close();
                Info.movingEnemy1 = 20;
                Info.movingEnemy2 = 25;
                Info.movelessEnemy1 = 25;
                Info.movelessEnemy2 = 30;
                dispose();
                Main.startGame();
            }
            if (e.getSource().equals(hard))
            {
                Info.Quest=31;
                Info.mapLocation=("src/maps/map"+Info.Quest+".map");
               // SoundMaker.inGameSound.stop();
              //  SoundMaker.inGameSound.close();
                Info.movingEnemy1 = 25;
                Info.movingEnemy2 = 30;
                Info.movelessEnemy1 = 35;
                Info.movelessEnemy2 = 40;
                dispose();
                Main.startGame();
            }
            if (e.getSource().equals(host))
            {
                Info.Quest=41;
                Info.mapLocation=("src/maps/map"+Info.Quest+".map");
                Network.startServer(frame);
                Network.turn = 0;
                Network.isServer = true;
             //   SoundMaker.inGameSound.stop();
              //  SoundMaker.inGameSound.close();
            }
            if (e.getSource().equals(connect))
            {
                Info.Quest=41;
                Info.mapLocation=("src/maps/map"+Info.Quest+".map");
                Network.connectToOthers(frame);
                Network.turn = 1;
                Network.isServer = false;
              //  SoundMaker.inGameSound.stop();
              //  SoundMaker.inGameSound.close();
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        Image img = null;
        try
        {
            img = ImageIO.read(new File("src/images/mainPage.png"));
            //img = ImageIO.read(new File("src/images/mainPage2.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        g.drawImage(img, 0, 0, null);
        newGame.repaint();
        resume.repaint();
        host.repaint();
        connect.repaint();
        easy.repaint();
        medium.repaint();
        hard.repaint();
    }
}
