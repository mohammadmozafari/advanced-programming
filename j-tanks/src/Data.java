
import java.io.Serializable;
import java.util.Vector;

/**
 * each time the server or the client want to send data , the information needed to be sent is saved
 * in an object of this class .
 * holding tanks information and walls and bonuses (which are kept in data quantum)
 * @author soroush mortazavi
 */
public class Data implements Serializable
{
    private Vector<Tank> tanks;
    private Vector<DataQuantom> walls;
    private Vector<DataQuantom> bonuses;

    public Data()
    {
        tanks = new Vector<>();
        walls = new Vector<>();
        bonuses = new Vector<>();
    }

    public Vector<Tank> getTanks()
    {
        return tanks;
    }

    /**
     * this method is used to save the information about the tanks which are about to be sent
     * each time the tank before the last one is deleted.
     * so that client or server won't force the other's tank position in a place they aren't
     * @param tanks is the vector of the tanks to be sent to the other side
     */
    public void setTanks(Vector<Tank> tanks)
    {
        if (Network.isServer && Network.firstConnection)
        {
            Network.firstConnection = false;
        }
        else
        {
            tanks.remove(tanks.size() - 2);
        }
        this.tanks = tanks;
    }
    public Vector<DataQuantom> getWalls()
    {
        return walls;
    }

    public void setWalls(Vector<DataQuantom> walls)
    {
        this.walls = walls;
    }

    public Vector<DataQuantom> getBonuses()
    {
        return bonuses;
    }


    public void setBonuses(Vector<DataQuantom> bonuses)
    {
        this.bonuses = bonuses;
    }
}
