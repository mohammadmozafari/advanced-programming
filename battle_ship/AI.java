import java.util.Random;

public class AI extends Player
{
    private final int EMPTY, HIT, NOT_HIT, MISSED;
    private int[] table;
    /**
     * Sets the information of a player.
     * @param exactShot whether the shots are accurate or not
     * @param name the name of the player
     */
    public AI(boolean exactShot, String name)
    {
        super(exactShot, name);
        EMPTY = 0;
        HIT = 1;
        NOT_HIT = 2;
        MISSED = 3;
    }

    /**
     * Fills the board randomly with 5 ships.
     */
    @Override
    public void setBoard()
    {
        boolean vertical;
        int startRow, startColumn, length;
        Random rand = new Random();

        for (int i = 0 ; i < 5 ; i++)
        {
            while (true)
            {
                vertical = rand.nextBoolean();
                startRow = rand.nextInt(10);
                startColumn = rand.nextInt(10);
                if (!vertical)
                {
                    if (startColumn == 9) continue;
                    length = rand.nextInt(9 - startColumn) + 2;
                }
                else
                {
                    if (startRow == 9) continue;
                    length = rand.nextInt(9 - startRow) + 2;
                }

                if (super.getMap().addShip(startRow, startColumn, length, vertical)) break;
            }
        }
    }

    @Override
    public int move(int[] table)
    {
        this.table = table;
        int a = think();
        super.increaseMoves();
        return a;
    }


    /**
     * This part is the part computer kind of thinks to choose a shot based on the previous shots.
     * @return the numeric place to shoot at
     */
    private int think()
    {
        int row, column, up, down, right, left;
        Random rand = new Random();

        if (super.getNumOfMoves() == 0)
        {
            row = 4 + rand.nextInt(2);
            column = 4 + rand.nextInt(2);

            return Board.findNumericPlace(row, column, 10);
        }

        else
        {
            for (int i = 0 ; i < 100 ; i++)
            {
                if (table[i] == HIT)
                {
                    row = Board.findRow(i, 10);
                    column = Board.findColumn(i, 10);

                    if (Board.validShot2(table, row + 1, column) && Board.validShot(table, row - 1, column)
                            && table[Board.findNumericPlace(row + 1, column, 10)] == HIT)
                        return Board.findNumericPlace(row - 1,column, 10);

                    else if (Board.validShot(table, row + 1, column) && Board.validShot2(table, row - 1, column)
                            && table[Board.findNumericPlace(row - 1, column, 10)] == HIT)
                        return Board.findNumericPlace(row + 1,column, 10);

                    else if (Board.validShot2(table, row, column + 1) && Board.validShot(table, row, column - 1)
                            && table[Board.findNumericPlace(row, column + 1, 10)] == HIT)
                        return Board.findNumericPlace(row,column - 1, 10);

                    else if (Board.validShot(table, row, column + 1) && Board.validShot2(table, row, column - 1)
                            && table[Board.findNumericPlace(row, column - 1, 10)] == HIT)
                        return Board.findNumericPlace(row,column + 1, 10);

                    else
                    {
                        if (Board.validShot(table, row + 1, column)) return Board.findNumericPlace(row + 1,column, 10);
                        else if (Board.validShot(table, row - 1, column)) return Board.findNumericPlace(row - 1,column, 10);
                        else if (Board.validShot(table, row, column + 1)) return Board.findNumericPlace(row,column + 1, 10);
                        else if (Board.validShot(table, row, column - 1)) return Board.findNumericPlace(row,column - 1, 10);
                    }

                }
            }

            while (true)
            {
                row = rand.nextInt(10);
                column = rand.nextInt(10);

                if (Board.validShot(table, row, column)) return Board.findNumericPlace(row, column, 10);
            }
        }

    }
}
