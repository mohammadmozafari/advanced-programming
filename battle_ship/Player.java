/**
 * This abstract class is the base class for all players (AI and Human).
 * It holds the info of the player and defines some common behavior.
 *
 * @author Mohammad Mozafari
 */
public abstract class Player
{
    //fields
    private boolean exactShot;
    private int numOfMoves;
    private Board map;
    private String name;

    /**
     * Sets the values for fields.
     * @param exactShot whether the shots are accurate or not
     * @param name the name of the player
     */
    public Player(boolean exactShot, String name)
    {
        this.exactShot = exactShot;
        this.name = name;
        map = new Board(10, 10);
        numOfMoves = 0;
    }

    public abstract int move(int[] table);
    public abstract void setBoard();

    /**
     * Checks if the player has lost the game or not.
     * @return whether the player has lost or not
     */
    public boolean lost()
    {
        return getMap().lost();
    }

    /**
     * @return the name of the player
     */
    protected String getName()
    {
        return name;
    }

    /**
     * @return the board of the player
     */
    protected Board getMap()
    {
        return map;
    }

    /**
     * @return whether the shot is exact or not
     */
    protected boolean isExactShot()
    {
        return exactShot;
    }

    protected int getNumOfMoves()
    {
        return numOfMoves;
    }

    protected void increaseMoves()
    {
        numOfMoves++;
    }
}
