import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds the information of a particular gun. These information includes ammo and gun damage.
 *
 * @author Mohammad Mozafari
 */
public class Weapon implements Serializable
{
    private int ammo;
    private int level, shape;
    private int levelOneDamage;
    /*
    imgPath -> the path of the pipe
    display -> the path of the image representing the weapon
    bulletImage -> the path of the image of the bullets of this weapon
     */
    private String imgPath;
    private String display;
    private String bulletImage;

    public Weapon(int ammo, int level, int levelOneDamage, String imgPath, String display, String bulletImage)
    {
        this.levelOneDamage = levelOneDamage;
        this.imgPath = imgPath;
        this.display = display;
        this.bulletImage = bulletImage;
        setAmmo(ammo);
        this.level = level;
        shape = level;
    }

    public void setAmmo(int ammo)
    {
        this.ammo = ammo;
    }

    public void addLevel()
    {
        level++;
        if (level <= 2)
            shape++;
    }

    /**
     * Returns the path of a proper pipe image according to imgPath and the level of the weapon
     *
     * @return the path of a proper pipe image
     */
    public String getImgPath()
    {
        return imgPath + shape + ".png";
    }

    public String getBulletImage()
    {
        return bulletImage;
    }

    public String getDisplay()
    {
        return display;
    }

    public int getAmmo()
    {
        return ammo;
    }

    public int getDamage()
    {
        return levelOneDamage * level;
    }

    public int getLevelOneDamage()
    {
        return levelOneDamage;
    }

    public int getLevel()
    {
        return level;
    }
}
