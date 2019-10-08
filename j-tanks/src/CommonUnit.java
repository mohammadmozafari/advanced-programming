import java.awt.*;
import java.io.Serializable;

/**
 * this class is used to keep the information of common units of the map such as : walls (all kind) , ground and trees
 *
 * @author seyyed soroush mortazavi moghaddam
 */
public class CommonUnit implements Serializable
{
    protected int height, width;
    protected Point position;
    protected Point AbsolutePos;
    protected String imgPath;
    public CommonUnit(Point position, String imgPath)
    {
        this.position = position;
        AbsolutePos = position;
        this.imgPath = imgPath;
        if (Info.photos.get(imgPath) != null)
        {
            height = Info.photos.get(imgPath).getHeight(null);
            width = Info.photos.get(imgPath).getWidth(null);
        }
    }

    /**
     * this method is used to paint the units
     * @param g2d is the pen given to the class to draw images
     */
    public void paintUnit(Graphics2D g2d)
    {
//        System.out.println(Info.xDif+" , "+Info.yDif);
//        if ((position.x>=(-100) && position.x <= ( Info.tankGlobalPositionX + 1400) &&
//                position.x >= ( Info.tankGlobalPositionX - 1400) &&
//                position.x<Info.map[0].length*Info.unitWidth) &&
//                (position.y>=(-100) && position.y <= (Info.tankGlobalPositionY + 900) &&
//                position.y >= (Info.tankGlobalPositionY - 900) &&
//                position.y<=Info.map[0].length*Info.unitHeight))
            g2d.drawImage(Info.photos.get(imgPath), position.x-(Info.tankGlobalPositionX-Info.tankRelativePositionX),
                    position.y-(Info.tankGlobalPositionY-Info.tankRelativePositionY), null);
    }

    public Point getAbsolutePos()
    {
        return AbsolutePos;
    }

}
