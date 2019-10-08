import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.Serializable;

/**
 * this class holds the information about the bullets
 * each tank has its own bullets
 * @author moahmmad mozafary
 */
public class Bullet implements Crashable, Serializable
{
    private int locX, locY, width, height, damage;
    private int speed;
    private double angle;
    private String bulletPath;
    private double remainingX, remainingY;

    public Bullet(int x, int y, int length, double angle, String imagePath, int damage)
    {
        this.angle = angle;
        this.damage = damage;
        bulletPath = imagePath;
        width = Info.photos.get(bulletPath).getWidth(null);
        height = Info.photos.get(bulletPath).getHeight(null);
        locX = (int) (x + length * Math.sin(angle));
        locY = (int) (y - length * Math.cos(angle));
        locY -= height;
        speed = Info.speedConstant / width;
    }

    public int getDamage()
    {
        return damage;
    }

    /**
     * This method changes the location of the bullet. according to bullet's speed
     */
    public void move()
    {
        double newX = speed * Math.sin(angle);
        double newY = speed * Math.cos(angle);

        remainingX += newX - (int) newX;
        remainingY += newY - (int) newY;

        if (Math.abs(remainingX) > 1)
        {
            newX += (int) remainingX;
            remainingX = remainingX - (int) remainingX;
        }

        if (Math.abs(remainingY) > 1)
        {
            newY += (int) remainingY;
            remainingY = remainingY - (int) remainingY;
        }

        locX += (int) newX;
        locY -= (int) newY;
    }

    /**
     * This method draws the bullet based on its location and angle.
     *
     * @param g2d the pen to draw with
     */
    public void drawBullet(Graphics2D g2d)
    {
        AffineTransform old = g2d.getTransform();
        AffineTransform newAt = new AffineTransform();
        newAt.rotate(angle, (locX + width / 2) - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                (locY + height / 2) - (Info.tankGlobalPositionY - Info.tankRelativePositionY));
        g2d.setTransform(newAt);
        g2d.drawImage(Info.photos.get(bulletPath), locX - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                locY - (Info.tankGlobalPositionY - Info.tankRelativePositionY), null);
        g2d.setTransform(old);
    }

    /**
     * This method checks to see if the location of the bullet is inside the main frame.
     *
     * @param widthMax  the width of the main frame
     * @param heightMax the height of the main frame
     * @return true if the bullet is in frame range and false otherwise
     */
    public boolean isInRange(int widthMax, int heightMax)
    {
        if ((locX - (Info.tankGlobalPositionX - Info.tankRelativePositionX) < 0 ||
                locX - (Info.tankGlobalPositionX - Info.tankRelativePositionX) > widthMax) ||
                (locY - (Info.tankGlobalPositionY - Info.tankRelativePositionY) < 0 ||
                        locY - (Info.tankGlobalPositionY - Info.tankRelativePositionY) > heightMax))
            return false;
        return true;
    }

    @Override
    public Area getArea()
    {
        AffineTransform at = new AffineTransform();
        at.rotate(angle, locX- (Info.tankGlobalPositionX - Info.tankRelativePositionX) + width / 2,
                locY - (Info.tankGlobalPositionY - Info.tankRelativePositionY) + height / 2);
        Area area = new Area(new Rectangle(locX - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                locY - (Info.tankGlobalPositionY - Info.tankRelativePositionY), width, height));
        area = area.createTransformedArea(at);
        //GameFrame.rectangles.add(area);
        return area;
    }

    @Override
    public Point getCenter()
    {
        Point center = new Point();
        center.x = (locX + width / 2 - (Info.tankGlobalPositionX - Info.tankRelativePositionX));
        center.y = (locY + height / 2 - (Info.tankGlobalPositionY - Info.tankRelativePositionY));
        return center;
    }
}
