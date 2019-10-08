/**
 * each moving enemy is created in this class
 * this extends from tank class
 * @author mohammad mozafary
 */
public class MovingTank extends Tank
{
    final int moves = 150;
    int counter = 0;
    int flag = 1;
    boolean hor;

    public MovingTank(int x, int y, int health, String imgPath, String pipeImage, int xMiddle, int yMiddle, int pipeX, int pipeY, boolean hor)
    {
        super(x, y, health, imgPath, pipeImage, xMiddle, yMiddle, pipeX, pipeY);
        this.hor = hor;
        if (hor) setTankType(3);
        else setTankType(4);
    }

    public void move(int step)
    {
        if (hor)
            locX += flag * step;
        else
            locY += flag * step;
        counter++;
        if (counter >= moves)
        {
            flag *= -1;
            counter = 0;
        }
    }

    public void changeDirection()
    {
        flag *= -1;
        counter = moves - counter;
    }
}
