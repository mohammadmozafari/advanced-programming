/*** In The Name of Allah ***/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The window on which the rendering is performed.
 * This example uses the modern BufferStrategy approach for double-buffering,
 * actually it performs triple-buffering!
 * For more information on BufferStrategy check out:
 * http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 * http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 *
 * @author mohammad mozafary and soroush mortazavi
 */
public class GameFrame extends JFrame
{

    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio

    //uncomment all /*...*/ in the class for using Tank icon instead of a simple circle
    /*private BufferedImage image;*/

    private long lastRender;
    private ArrayList<Float> fpsHistory;

    private BufferStrategy bufferStrategy;
    //public static ArrayList<Area> rectangles = new ArrayList<Area>();

    private Data data = new Data();

    public GameFrame(String title)
    {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        lastRender = -1;
        fpsHistory = new ArrayList<>(100);
        data = new Data();
    }

    /**
     * This must be called once after the JFrame is shown:
     * frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy()
    {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }


    /**
     * Game rendering with triple-buffering using BufferStrategy.
     */
    public void render(GameState state)
    {
        // Render single frame
        do
        {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do
            {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try
                {
                    doRendering(graphics, state);
                } finally
                {
                    // Dispose the graphics
                    graphics.dispose();
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());

            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();

            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state)
    {
        //System.out.println("____________");
        // Draw background
//        Image img = null;
//        try
//        {
//            img = ImageIO.read(new File("src/images/ground.png"));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }

        g2d.setColor(Color.GRAY);
        //g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        for (Ground g : state.getGround())
            g.paintUnit(g2d);

        //state.getTanks().add(state.ownTank);
        Vector<Bullet> bullets;
        for (int i = 0; i < state.getTanks().size(); i++)
        {
            state.getTanks().get(i).drawTank(g2d);
            bullets = state.getTanks().get(i).getBullets();
//            if (state.getTanks().get(i) instanceof OwnTank)
//                System.out.println(bullets.size());
            for (int j = 0 ; j < bullets.size() ; j++)
                bullets.get(j).drawBullet(g2d);
            //System.out.print(state.getTanks().get(i).getLocX() + " , " + state.getTanks().get(i).getLocY() + "|");
        }
        //System.out.println(state.getTanks().get(state.getTanks().size()-1).getLocX() +
        //        " , " + state.getTanks().get(state.getTanks().size()-1).getLocY() + "|");
        for (Wall wall : state.getWalls())
            wall.paintUnit(g2d);
        // Draw ball
//        g2d.setColor(Color.BLACK);
//		g2d.fillOval(state.locX, state.locY, state.diam, state.diam);


        //drawing tanks and bullets
//        Vector<Bullet> bullets = state.();
//        for (int i = 0; i < bullets.size(); i++)
//        {
//            bullets.get(i).drawBullet(g2d);
////			state.updateBulletsStat(bullets.get(i));
//        }
        Vector<Tree> trees = state.getTrees();
        for (int i = 0; i < trees.size(); i++)
        {
            trees.get(i).paintUnit(g2d);
        }
        for (Bonus b : state.getBonuses())
            b.paintUnit(g2d);
//        state.ownTank.drawTank(g2d);
        state.ownTank.drawTankProps(g2d);


        // Print FPS info
        long currentRender = System.currentTimeMillis();
        if (lastRender > 0)
        {
            fpsHistory.add(1000.0f / (currentRender - lastRender));
            if (fpsHistory.size() > 100)
            {
                fpsHistory.remove(0); // remove oldest
            }
            float avg = 0.0f;
            for (float fps : fpsHistory)
            {
                avg += fps;
            }
            avg /= fpsHistory.size();
            String str = String.format("Average FPS = %.1f , Last Interval = %d ms",
                    avg, (currentRender - lastRender));
            g2d.setColor(Color.CYAN);
            g2d.setFont(g2d.getFont().deriveFont(18.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            int strHeight = g2d.getFontMetrics().getHeight();
            g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, strHeight + 50);
            Info.xDif = 0;
            Info.yDif = 0;
        }
        lastRender = currentRender;
        // Print user guide
        String userGuide
                = "Use the MOUSE or ARROW KEYS to move the BALL. "
                + "Press ESCAPE to end the game.";
        g2d.setFont(g2d.getFont().deriveFont(18.0f));
        g2d.drawString(userGuide, 10, GAME_HEIGHT - 10);
        // Draw GAME OVER
//        for (Area rectangle :rectangles)
//        {
//            g2d.fill(rectangle);
//        }
//        //rectangles.removeAll(rectangles);
    }

    private boolean ItemInRange(int ItemPositionX, int itemPositionY, int tankCenterLocX, int tankCenterLocY)
    {
        Point checkPoint = new Point((tankCenterLocX) / 2, (tankCenterLocY) / 2);
//        System.out.println("mouse : x: " + mouseX +" y: "+ mouseY);
//        System.out.println("tank : x: " + tankCenterLocX +" y: "+ tankCenterLocY);
//        System.out.println("check point : x : "+checkPoint.x + " y : " + checkPoint.y);
//        try
//        {
//            Thread.sleep(1000);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
        if (ItemPositionX < checkPoint.x + Info.mainFrameWidth / 2 + Info.unitWidth &&
                ItemPositionX > checkPoint.x - (Info.mainFrameWidth / 2 + Info.unitWidth))
            if (itemPositionY < checkPoint.x + Info.mainFrameHeight / 2 + Info.unitHeight &&
                    itemPositionY > checkPoint.x - (Info.mainFrameHeight / 2 + Info.unitHeight))
                return true;
        return false;
    }


}
