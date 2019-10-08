import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * this class is a JFrame which will be shown once the game is over (either lost or won)
 * booleans are set to determine the type of game over (lost or won)
 */
public class EndFrame extends JFrame
{
    private JButton reTry;
    private JButton exit;
    private boolean win;

    public EndFrame(String title, boolean win) throws HeadlessException
    {
        super(title);
        setResizable(false);
        setLocation(300, 200);
        setSize(1280, 720);
        setLayout(null);
        this.win = win;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        exit = new JButton("exit");
        exit.setBackground(Color.darkGray);
        exit.setForeground(Color.white);
        exit.setLocation(500, 400);
        exit.setSize(85, 55);
        exit.setContentAreaFilled(false);
        exit.setOpaque(true);
        exit.setFocusPainted(false);
        exit.addActionListener(e ->
                System.exit(0));
        reTry = new JButton();
        if (!win)
            reTry.setText("reTry");
        else
            reTry.setText("Next Q");
        reTry.setBackground(Color.darkGray);
        reTry.setForeground(Color.white);
        reTry.setLocation(650, 400);
        reTry.setSize(85, 55);
        reTry.setContentAreaFilled(false);
        reTry.setOpaque(true);
        if (!win)
            reTry.addActionListener(e ->
            {
                dispose();
                new Menu();
            });
        else
            reTry.addActionListener(e ->
            {
                dispose();
                Info.Quest++;
                Info.mapLocation = ("src/maps/map" + Info.Quest + ".map");
                if (!Network.mPlayer)
                    Main.startGame();
                else
                {
                    try
                    {
                        Network.socket.close();
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    if (Network.isServer)
                        Network.startServer(this);
                    else
                        Network.connectToOthers(this);
                }
            });
        add(exit);
        add(reTry);
        setVisible(true);
    }

    /**
     * this method paints a picture on the back of the frame
     * if lost gameover.png and if won victory.png
     * @param g2d is the pen which will paint on the frame
     */
    @Override
    public void paint(Graphics g2d)
    {
        String str = "You are dead!";
        if (win)
            str = "Victory!";
        g2d.setColor(Color.RED);
        if (win)
            g2d.setColor(Color.GREEN);

        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(40.0f));
        int strWidth = g2d.getFontMetrics().stringWidth(str);
        Image img = null;
        try
        {
            img = ImageIO.read(new File("src/images/gameOverPage.png"));
            if (win)
                img = ImageIO.read(new File("src/images/winPage.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        g2d.drawImage(img, 0, 0, null);
        g2d.drawString(str, (1280 - strWidth) / 2, 360);
        exit.repaint();
        reTry.repaint();
    }
}
