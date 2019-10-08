import java.awt.*;
import java.io.Serializable;

/**
 * this class is used to keep the information of the weak walls and bonuses which are sent to the
 * server/client .
 * @author soroush mortazavi
 */
public class DataQuantom implements Serializable
{
    private Point AbsolutePos;
    private int state;

    public DataQuantom(Point absolutePos, int state)
    {
        AbsolutePos = absolutePos;
        this.state = state;
    }

    public Point getAbsolutePos()
    {
        return AbsolutePos;
    }

    public int getState()
    {
        return state;
    }
}
