import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;

/**
 * this class holds the information for bounce items
 * including machineGun cartridges and aid packs and upgrade and ending points
 * @ mohammad mozafary
 */
public class Bonus extends CommonUnit implements Crashable, Serializable
{
    public final int machineGunCart = 1;
    public final int cannonShell = 2;
    public final int aid = 3;
    public final int upgrade = 4;
    public final int endingPoint = 5;

    private int bonusType;

    public Bonus(Point position, String img, int bonusType)
    {
        super(position, img);
        this.bonusType = bonusType;
    }

    /**
     * this method is invoked once a bounce is absorbed by the player
     * @param tank is tank who absorbed the bounce
     */
    public void apply(OwnTank tank)
    {
        if (bonusType == cannonShell)
        {
            tank.getWeapons().get(0).setAmmo(tank.getWeapons().get(0).getAmmo() + 50);
            SoundMaker.play("src/Sounds/select.wav");
        }
        else if (bonusType == machineGunCart)
        {
            tank.getWeapons().get(1).setAmmo(tank.getWeapons().get(1).getAmmo() + 100);
            SoundMaker.play("src/Sounds/select.wav");
        }
        else if (bonusType == aid)
        {
            tank.setHealth(tank.getHealth() + 25);
            SoundMaker.play("src/Sounds/repair.wav");
        }
        else if (bonusType == upgrade)
        {
            tank.getActiveWeapon().addLevel();
            tank.changeWeaponImage(tank.getActiveWeaponIndex());
            SoundMaker.play("src/Sounds/agree.wav");
        }
        else if (bonusType == endingPoint)
        {
            //game over !
            System.out.println("YOU WIN");
        }
    }

    public int getBonusType()
    {
        return bonusType;
    }

    public void setBonusType(int bonusType)
    {
        this.bonusType = bonusType;
    }

    @Override
    public Area getArea()
    {
        Area area = new Area(new Rectangle(position.x - (Info.tankGlobalPositionX - Info.tankRelativePositionX)+ (100 - width) / 2,
                position.y- (Info.tankGlobalPositionY - Info.tankRelativePositionY)+ (100 - height) / 2, width, height));
        return area;

    }

    @Override
    public Point getCenter()
    {
        return new Point(position.x + 50- (Info.tankGlobalPositionX - Info.tankRelativePositionX),
                position.y + 50- (Info.tankGlobalPositionY - Info.tankRelativePositionY));
    }

    @Override
    public void paintUnit(Graphics2D g2d)
    {
        g2d.drawImage(Info.photos.get(imgPath), position.x - (Info.tankGlobalPositionX - Info.tankRelativePositionX)+ (100 - width) / 2,
                position.y- (Info.tankGlobalPositionY - Info.tankRelativePositionY)+ (100 - height) / 2, null);
    }
}
