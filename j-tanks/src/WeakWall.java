import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.geom.Area;

/**
 * this class is for weak walls
 * @author seyyed soroush mortazavi moghaddam
 */
public class WeakWall extends Wall implements Crashable
{
    /**
     * states represents the amount of damaged dealt to the wall
     * 0 means nothing , 1 means some , 2 means too much
     * after 2 , the wall is destroyed
     */
    private int state = 0;
    private int damageCounter = 0;
    private String[] imgPaths;

    public WeakWall(Point position, String imgPath)
    {
        super(position, imgPath);
        imgPaths = new String[4];
        for (int i = 0; i <4; i++)
        {
            switch (i)
            {
                case 0 :
                    imgPath="src/images/weakWall_L3.png";
                    break;
                case 1 :
                    imgPath="src/images/weakWall_L2.png";
                    break;
                case 2 :
                    imgPath="src/images/weakWall_L1.png";
                    break;
                default:
                    imgPath="src/images/ground.png";
            }
            imgPaths[i] = imgPath;
        }

    }

    public boolean isDestroyed()
    {
        return (state > 2);
    }

    @Override
    public Area getArea()
    {
        return super.getArea();
    }

    @Override
    public Point getCenter()
    {
        return super.getCenter();
    }

    /**
     * when shot , the states of the wall increases
     */
    public void getShot(int damage)
    {
        SoundMaker.play("src/Sounds/softwall.wav");
        damageCounter+=damage;
        if (damageCounter>20)
        {
            state++;
            damageCounter=0;
        }
    }

    @Override
    public void paintUnit(Graphics2D g2d)
    {
        imgPath = imgPaths[state];
        super.paintUnit(g2d);
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}
