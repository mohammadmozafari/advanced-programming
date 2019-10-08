
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * this class is used to keep the information of the player's tank
 * @author mohammad mozafary
 *
 */
public class OwnTank extends Tank implements Crashable, Serializable
{
    private final int maxHealth;
    private int relativeX, relativeY, numOfLives, activeWeaponIndex;
    private double tankAngle;
    private Vector<Weapon> weapons;
    private Vector<Bullet> bullets = new Vector<>();
    private String[] pipeImages, bulletImages, weaponDisplays;
    private String heart;
    private boolean isServer;
    private boolean win=false;

    public OwnTank(int x, int y, int health, int numOfLives, Vector<Weapon> weapons, String imgPath, int xMiddle, int yMiddle)
    {
        super(x, y, health, imgPath, null, xMiddle, yMiddle, Info.ownTankPipeLocX, Info.ownTankPipeLocX);
        relativeX = x;
        relativeY = y;
        maxHealth = health;
        this.numOfLives = numOfLives;
        setWeapons(weapons);

        pipeImages = new String[weapons.size()];
        bulletImages = new String[weapons.size()];
        weaponDisplays = new String[weapons.size()];
        for (int i = 0 ; i < weapons.size() ; i++)
        {
            pipeImages[i] = weapons.get(i).getImgPath();
            bulletImages[i] = weapons.get(i).getBulletImage();
            weaponDisplays[i] = weapons.get(i).getDisplay();
        }
        heart = Info.heartPath;
    }

    public void setNumOfLives(int numOfLives)
    {
        this.numOfLives = numOfLives;
    }

    public void setWeapons(Vector<Weapon> weapons)
    {
        this.weapons = weapons;
    }

    public void setTankAngle(double tankAngle)
    {
        if (tankAngle >= 2 * Math.PI)
            this.tankAngle = tankAngle - 2 * Math.PI;
        else if (tankAngle < 0)
            this.tankAngle = tankAngle + 2 * Math.PI;
        else
            this.tankAngle = tankAngle;
    }

    public void setRelativeX(int relativeX)
    {
        this.relativeX = relativeX;
        Info.tankRelativePositionX = relativeX;
    }

    public void changeOnlyRelativeX(int relativeX)
    {
        this.relativeX = relativeX;
    }
    public void changeOnlyRelativeY(int relativeY)
    {
        this.relativeY = relativeY;
    }

    public void setRelativeY(int relativeY)
    {
        this.relativeY = relativeY;
        Info.tankRelativePositionY = relativeY;
    }

    @Override
    public void setHealth(int health)
    {
        if (health > maxHealth)
            return;
        else if (health <= 0 && numOfLives > 0)
        {
            this.health = maxHealth;
            numOfLives--;
        }
        else
            super.setHealth(health);
    }

    public void setServer(boolean server)
    {
        isServer = server;
    }

    public void setWin(boolean win)
    {
        this.win = win;
    }

    public Vector<Weapon> getWeapons()
    {
        return weapons;
    }

    public double getTankAngle()
    {
        return tankAngle;
    }

    public boolean isWin()
    {
        return win;
    }

    public int getRelativeX()
    {
        return relativeX;
    }

    public int getRelativeY()
    {
        return relativeY;
    }

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public int getNumOfLives()
    {
        return numOfLives;
    }

    public int getActiveWeaponIndex()
    {
        return activeWeaponIndex;
    }

    @Override
    public Vector<Bullet> getBullets()
    {
        return bullets;
    }

    /**
     * @return the current weapon of the tank
     */
    public Weapon getActiveWeapon()
    {
        return weapons.get(activeWeaponIndex);
    }

    /**
     * This method takes a pen a draws the image of the tank and its pipe with data stored in fields.
     * This method also draws the status of every weapon in the top left corner.
     *
     * @param g2d the pen to draw with
     */
    public void drawTank(Graphics2D g2d)
    {
        AffineTransform oldAt = g2d.getTransform();
        AffineTransform newAt = new AffineTransform();
        newAt.rotate(getTankAngle(), getCenter().x, getCenter().y);

        g2d.setTransform(newAt);
        g2d.drawImage(Info.photos.get(super.imgPath), getRelativeX(), getRelativeY(), null);

//        System.out.println(getLocX() + middleX);
//        System.out.println(getLocY() + middleY);

        newAt = new AffineTransform();
        newAt.rotate(getPipeAngle(), getCenter().x, getCenter().y);
        g2d.setTransform(newAt);
        //System.out.println("tank done");
        g2d.drawImage(Info.photos.get(pipeImages[activeWeaponIndex]), relativeX + Info.ownTankPipeLocX, relativeY + Info.ownTankPipeLocY, null);
        g2d.setTransform(oldAt);
    }

    public Point tankDif(int x, int y)
    {
        if (x > (Info.mainFrameWidth / 2 + 50))
        {
            Info.xDif = x - (Info.mainFrameWidth / 2 + 50);
        } else if (x < (Info.mainFrameWidth / 2 - 150))
        {
            Info.xDif = x - (Info.mainFrameWidth / 2 - 150);
        }
        if (y > (Info.mainFrameHeight / 2))
        {
            Info.yDif = (y - (Info.mainFrameHeight / 2));
        } else if (y < (Info.mainFrameHeight / 2 - 140))
        {
            Info.yDif = (y - (Info.mainFrameHeight / 2 - 140));
        }
        //  System.out.println((Info.mainFrameHeight / 2 - 140));
        //  System.out.println((Info.mainFrameHeight / 2 ));
        //  System.out.println("x : "+ Info.xDif + " y : "+ Info.yDif);
        return new Point(Info.xDif, Info.yDif);
    }

    public void drawTankProps(Graphics2D g2d)
    {
        int weaponDisplayHeight = 140, weaponDisplayWidth = 80;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2);

        g2d.setColor(Color.RED);
        for (int i = 0; i < weaponDisplays.length; i++)
        {
            g2d.setComposite(ac);
            g2d.setColor(Color.yellow);
            g2d.fillRect(30 + i * (weaponDisplayWidth + 10), 60, weaponDisplayWidth, weaponDisplayHeight);
            g2d.setColor(Color.RED);
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.6);
            g2d.setComposite(ac);
            g2d.drawImage(Info.photos.get(weaponDisplays[i]), 40 + i * (weaponDisplayWidth + 10), 70, null);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString(weapons.get(i).getAmmo() + "", 55 + i * (weaponDisplayWidth + 10), 190);
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2);
        }
        for (int i = 0; i < numOfLives; i++)
        {
            g2d.drawImage(Info.photos.get(heart), Info.mainFrameWidth - 60 - i * (42), 60, null);
        }

        g2d.setColor(Color.green);
        g2d.drawRect(Info.mainFrameWidth - 130, 100, 100, 20);
        g2d.fillRect(Info.mainFrameWidth - 30 - health, 100, health, 20);

        g2d.setFont(new Font("Arial", Font.PLAIN, 15));
        ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1);
        g2d.setComposite(ac);
        g2d.setColor(Color.BLACK);
    }

    /**
     * This method rotates the tank based on the tank motion type.
     *
     * @param direction
     */
    public void rotate(int direction)
    {
        double angleStep = Math.PI / 100;
        double target = direction * Math.PI / 4;
        int flag = 1;

        if (Math.cos(tankAngle - target) > 0)
        {
            if (Math.sin(tankAngle - target) > 0)
                flag = -1;
            else flag = +1;
        }
        else if (Math.cos(tankAngle - target) != -1)
        {
            if (Math.sin(tankAngle - target) > 0)
                flag = +1;
            else
                flag = -1;
        }

        if (Math.cos(tankAngle - target) != 1 && Math.cos(tankAngle - target) != -1)
            setTankAngle(tankAngle + (flag * angleStep));
    }

    public void changeWeapon()
    {
        activeWeaponIndex = (activeWeaponIndex + 1) % 2;
    }

    @Override
    public Area getArea()
    {
        AffineTransform at = new AffineTransform();
        at.rotate(tankAngle, getCenter().x, getCenter().y);
        Area area = new Area(new Rectangle(getRelativeX(), getRelativeY(), width, height));
        area = area.createTransformedArea(at);
        return area;
    }

    @Override
    public Point getCenter()
    {
        return new Point(getRelativeX() + middleX, getRelativeY() + middleY);
    }

    @Override
    public void fire()
    {
        if (getActiveWeapon().getAmmo() <= 0)
            return;
        getActiveWeapon().setAmmo(getActiveWeapon().getAmmo() - 1);

        if ((getActiveWeapon().getLevelOneDamage())==13)
            SoundMaker.play("src/Sounds/cannon.wav");
        if ((getActiveWeapon().getLevelOneDamage())==8)
            SoundMaker.play("src/Sounds/mashingun.wav");
        Bullet bullet = new Bullet(Info.tankGlobalPositionX + 40,Info.tankGlobalPositionY + 65, 80, pipeAngle,
                getActiveWeapon().getBulletImage(), getActiveWeapon().getDamage());

        bullets.add(bullet);
    }
    public void timer()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
//                System.out.println("____________");
                // System.out.println("A ( " + getLocX() + " , " + getLocY() + " )\n" +
//                System.out.println("A ( " + getRelativeX()+ " , " + getRelativeY() + " )");
//                System.out.println("B ( " + Info.tankRelativePositionX + " , " + Info.tankRelativePositionY + " )");
//                System.out.println("Absolute :x : " + locX + " y : " + locY +"\nRelative :x : " + relativeX + " y : " + relativeY +"\n____________");
            }
        },0,300);
    }

    public void changeWeaponImage(int index)
    {
        pipeImages[index] = getActiveWeapon().getImgPath();
    }
}
