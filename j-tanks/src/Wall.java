
import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;

/**
 * this class is for strong walls (later the weak ones extend from it)
 * @author seyyed soroush mortazavy moghaddam
 */
public class Wall extends CommonUnit implements Crashable, Serializable
{
    public Wall(Point position, String imgPath)
    {
        super(position, imgPath);
    }

    @Override
    public Area getArea()
    {
        Area area = new Area(new Rectangle(position.x-(Info.tankGlobalPositionX-Info.tankRelativePositionX),
                position.y-(Info.tankGlobalPositionY-Info.tankRelativePositionY), width, height));
        //GameFrame.rectangles.add(area);
        return area;
    }

    @Override
    public Point getCenter()
    {
        Point center = new Point();
        center.x = position.x-(Info.tankGlobalPositionX-Info.tankRelativePositionX) + width / 2;
        center.y = position.y-(Info.tankGlobalPositionY-Info.tankRelativePositionY) + height / 2;
        return center;
    }
}
