
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * This class demonstrates a tank in the game. It holds information regarding the location, health, guns and etc.
 *
 * @author Mohammad Mozafari
 */
public class Tank implements Crashable, Serializable
{
    //fields
    protected final int middleX, middleY, pipeX, pipeY;
    protected int locX, locY, width, height, health, tankType;
    double rotationAngle = Math.PI / 2;

    //following angle are measured based on the negative side of y axis.
    protected double pipeAngle;
    protected String imgPath, pipeImage;
    //    protected transient Image img, pipeImage;
    private long lastShot;
    Vector<Bullet> bullets = new Vector<Bullet>();

    public Tank(int x, int y, int health, String imgPath, String pipeImage, int xMiddle,
                int yMiddle, int pipeX, int pipeY)
    {
        setLocX(x);
        setLocY(y);
        this.health = health;
        this.imgPath = imgPath;
        middleX = xMiddle;
        middleY = yMiddle;
        this.pipeX = pipeX;
        this.pipeY = pipeY;
        this.pipeImage = pipeImage;

        //System.out.println(imgPath);
        height = Info.photos.get(imgPath).getHeight(null);
        width = Info.photos.get(imgPath).getWidth(null);

    }

    public void setTankType(int tankType)
    {
        this.tankType = tankType;
    }

    //setters
    public void setLocX(int locX)
    {
        this.locX = locX;
    }

    public void setLocY(int locY)
    {
        this.locY = locY;
    }

    /**
     * This method calculates the new angle for the tank pipe according to the given coordinate and the tank coordinates.
     *
     * @param x x coordinent
     * @param y y coordinent
     */
    public void setPipeAngle(int x, int y)
    {
        double angle;
        double a;
        double b;

        if (x == getCenter().x)
        {
            if (y < getCenter().y)
                angle = 0;
            else
                angle = Math.PI;
        } else if (y == getCenter().y)
        {
            if (x < getCenter().x)
                angle = 3 * Math.PI / 2;
            else
                angle = Math.PI / 2;
        } else
        {
            a = Math.abs(x - getCenter().x);
            b = Math.abs(y - getCenter().y);

            angle = Math.atan2(a, b);
            if (x < getCenter().x && y > getCenter().y) angle = Math.PI + angle;
            else if (x > getCenter().x && y > getCenter().y) angle = Math.PI - angle;
            else if (x < getCenter().x && y < getCenter().y) angle = 2 * Math.PI - angle;
        }
        pipeAngle = angle;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    //getters
    public int getLocX()
    {
        return locX;
    }

    public int getLocY()
    {
        return locY;
    }

    public int getHealth()
    {
        return health;
    }

    public String getImgPath()
    {
        return imgPath;
    }

    public double getPipeAngle()
    {
        return pipeAngle;
    }

    public int getTankType()
    {
        return tankType;
    }

    public Vector<Bullet> getBullets()
    {
        return bullets;
    }

    /**
     * Every time the tank fires this method creates a new bullet based on the position of the tank and the angle of its pipe.
     * This method also decreases the ammo of the active weapon.
     *
     * @return the bullet created
     */
    public void fire()
    {
        if (new Date().getTime() - lastShot < 1000)
            return;
        lastShot = new Date().getTime();
//        boomSound(2);
        Bullet bullet = null;
        if (tankType == 3)
             bullet = new Bullet(locX + middleX + 10, locY + middleY + 15, 75, pipeAngle, Info.cannonPath, 25);
        if (tankType == 4)
            bullet = new Bullet(locX + middleX - 5, locY + middleY + 15, 75, pipeAngle, Info.cannonPath, 25);
        if (tankType == 1)
            bullet = new Bullet(locX + middleX - 10, locY + middleY + 15, 95, pipeAngle, Info.cannonPath, 25);
        if (tankType == 2)
            bullet = new Bullet(locX + middleX - 5, locY + middleY + 15, 75, pipeAngle, Info.cannonPath, 25);
        bullets.add(bullet);
    }

    public void drawTank(Graphics2D g2d)
    {
//        setLocX(getLocX() - Info.xDif);
//        setLocY(getLocY() - Info.yDif);

        AffineTransform at = new AffineTransform();
        AffineTransform old = g2d.getTransform();

        if (tankType == 2)
            rotationAngle = -Math.PI / 2;
        if (tankType == 3 || tankType == 4)
            rotationAngle *= 0;

        at.rotate(rotationAngle, getCenter().x, getCenter().y);
        g2d.setTransform(at);
        g2d.drawImage(Info.photos.get(imgPath), getLocX() - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                getLocY() - (Info.tankGlobalPositionY - Info.tankRelativePositionY), null);
        at.setTransform(old);
        at.rotate(pipeAngle, getCenter().x, getCenter().y);
        g2d.setTransform(at);
        g2d.drawImage(Info.photos.get(pipeImage), getLocX() + pipeX - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                getLocY() + pipeY - (Info.tankGlobalPositionY - Info.tankRelativePositionY), null);
        g2d.setTransform(old);
    }

    @Override
    public Area getArea()
    {
        Area area = new Area(new Rectangle(getLocX() - (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                getLocY() - (Info.tankGlobalPositionY - Info.tankRelativePositionY), width, height));
        return area;
    }

    @Override
    public Point getCenter()
    {
        Point center = new Point();
        center.x = getLocX() + middleX - (Info.tankGlobalPositionX - Info.tankRelativePositionX);
        center.y = getLocY() + middleY - (Info.tankGlobalPositionY - Info.tankRelativePositionY);
        return center;
    }

    public void timer()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                //System.out.println("____________");
                // System.out.println("A ( " + getLocX() + " , " + getLocY() + " )\n" +
                //        "B ( " + Info.tankGlobalPositionX + " , " + Info.tankGlobalPositionY+ " )");
//                System.out.println("Absolute :x : " + locX + " y : " + locY +"\nRelative :x : " + relativeX + " y : " + relativeY +"\n____________");
            }
        }, 0, 300);
    }

    public int getBulletDistance()
    {
        if (tankType == 1)
            return 17;
        if (tankType == 2)
            return 2;
        if (tankType == 3)
            return 38;
        if (tankType == 4)
            return 3;
        return 0;
    }

}

