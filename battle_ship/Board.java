import java.util.Random;

/**
 * This class reperesents a board and some related information about the board.
 * It also does some operation on the board such as adding ship, shooting a place, and printinf board
 * @author Mohammad Mozafari
 */
public class Board
{
    //fields
    private int[] places;
    private int rows, columns;
    private static final int EMPTY = 0, HIT = 1, NOT_HIT = 2, MISSED = 3;

    /**
     * This constructor builds the board based on the number of rows and columns.
     * Default status of the places is empty.
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public Board(int rows, int columns)
    {
        places = new int[rows*columns];
        this.rows = rows;
        this.columns = columns;
        for (int i = 0 ; i < places.length ; i++)
            places[i] = EMPTY;
    }

    /**
     * Adds a ship to the board. This opperation might be unsuccessful because of starting point or length.
     * @param startRow the row of start of ship
     * @param startColumn the column of start of ship
     * @param length the length of ship
     * @param vertical whether the ship is vertical in map or horizontal
     * @return whether the operation was successful or not
     */
    public boolean addShip(int startRow, int startColumn, int length, boolean vertical)
    {
        if (startRow < 0 || startColumn < 0 || startRow >= rows || startColumn >= columns)
            return false;
        if (length > 5 || length < 2)
            return false;
        if (vertical && startRow+length > rows)
            return false;
        if ((!vertical) && startColumn+length > columns)
            return false;

        if (vertical == true)
        {
            for (int i = startRow ; i < startRow + length ; i++)
            {
                if (places[findNumericPlace(i, startColumn, columns)] != EMPTY)
                   return false;
            }
            for (int i = startRow ; i < startRow + length ; i++)
            {
                if (places[findNumericPlace(i, startColumn, columns)] == EMPTY)
                    places[findNumericPlace(i, startColumn, columns)] = NOT_HIT;
            }
        }
        else
        {
            for (int i = startColumn ; i < startColumn + length ; i++)
            {
                if (places[findNumericPlace(startRow, i, columns)] != EMPTY)
                    return false;
            }
            for (int i = startColumn ; i < startColumn + length ; i++)
            {
                if (places[findNumericPlace(startRow, i, columns)] == EMPTY)
                    places[findNumericPlace(startRow, i, columns)] = NOT_HIT;
            }
        }
        return true;
    }

    /**
     * This methods shoots a place in map and changes its status accordingly.
     * @param row the row to shoot
     * @param column the column to shoot
     * @return 0->wrong input, 1->missed, 2->already shot, 3->hit
     */
    public int shoot(int row, int column, boolean exact)
    {
        int n = findNumericPlace(row, column, columns);
        int rowChange, columnChange, result;
        Random rand = new Random();

        if (row >= rows || row < 0 || column >= columns || column < 0) return 0;
        if (places[n] == MISSED || places[n] == HIT) return 2;

        if (!exact)
        {
            while (true)
            {
                rowChange = rand.nextInt(3) - 1;
                columnChange = rand.nextInt(3) - 1;
                result = shoot(row + rowChange, column + columnChange, true);
                if (result == 0) continue;
                else if (result == 1) return 1;
                else if (result == 2) continue;
                else return 3;
            }
        }

        if (places[n] == EMPTY)
        {
            places[n] = MISSED;
            return 1;
        }
        else
        {
            places[n] = HIT;
            return 3;
        }
    }

    /**
     * Checks if the player has lost the game or not.
     * @return whether the player has lost or not
     */
    public boolean lost()
    {
        int hit = 0, all = 0;
        for (int i = 0 ; i < rows*columns ; i++)
        {
            if (places[i] == NOT_HIT)
                all++;
            else if (places[i] == HIT)
            {
                all++;
                hit++;
            }
        }
        return (all == hit);
    }

    /**
     * This method returns some restricted data about the board.
     * So we use it in opponent mode.
     * @return the map in opponent mode.
     */
    public int[] getOpponentView()
    {
        int[] table = new int[rows*columns];
        for (int i = 0 ; i < rows*columns ; i++)
        {
            if (places[i] == EMPTY) table[i] = places[i];
            else if (places[i] == NOT_HIT) table[i] = EMPTY;
            else if (places[i] == HIT) table[i] = HIT;
            else table[i] = MISSED;
        }
        return table;
    }

    //Returns the entire board as a table
    public int[] getPlaces()
    {
        return places;
    }

    /**
     * Prints the map based on number of rows and columns and the table.
     * If opponent is true the map is shown in the opponent view.
     * @param opponent whether the map shall be shown in opponent view or not
     */
    public static void printBoard(int[] places, int columns, int rows, boolean opponent)
    {
        if (opponent)
        {
            System.out.println("**************** OPPONENT'S MAP ****************");
            System.out.println("**************************************************");
        }
        else
        {
            System.out.println("**************** PLAYER'S MAP ****************");
            System.out.println("******************************************");
        }

        for (int i = 0 ; i <= columns ; i++)
        {
            if (i == 0) System.out.print("  |");
            else System.out.print(" " + (i-1) + " |");
        }
        System.out.println();
        for (int i = 0 ; i <= columns ; i++)
        {
            if (i == 0) System.out.print("--|");
            else System.out.print("---+");
        }
        System.out.println();

        for (int i = 0 ; i < rows ; i++)
        {
            for (int j = 0 ; j <= columns ; j++)
            {
                if (j == 0) System.out.print(i + " |");
                else printPlace(places[findNumericPlace(i, j-1, columns)], opponent);
            }
            System.out.println();
            for (int j = 0 ; j <= columns ; j++)
            {
                if (j == 0) System.out.print("--|");
                else System.out.print("---+");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a place based on its status. It might be empty, part of ship, hit part or missed shot
     * @param place the status of a place
     * @param opponent whether it is in opponent view or not
     */
    private static void printPlace(int place, boolean opponent)
    {
        if (opponent == true)
        {
            if (place == EMPTY) System.out.print("   |");
            else if (place == HIT) System.out.print(ConsoleColors.GREEN_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "|");
            else if (place == NOT_HIT) System.out.print("   |");
            else if (place == MISSED) System.out.print(ConsoleColors.YELLOW_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "|");
        }
        else
        {
            if (place == EMPTY) System.out.print("   |");
            else if (place == HIT) System.out.print(ConsoleColors.RED_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "|");
            else if (place == NOT_HIT) System.out.print(ConsoleColors.BLUE_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "|");
            else if (place == MISSED) System.out.print(ConsoleColors.YELLOW_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "|");
        }
    }

    /**
     * Translates the 2 dimension coordinents to 1 dimension.
     * @param row the row
     * @param column the column
     * @return the 1 dimension coordinents
     */
    public static int findNumericPlace(int row, int column, int columns)
    {
        return columns*row + column;
    }

    public static int findColumn(int number, int columns)
    {
        return number%columns;
    }

    public static int findRow(int number, int columns)
    {
        return number/columns;
    }

    /**
     * A valid shot (type 1) is a shot that the point is in the board and is empty.
     * @param table the board to shoot at
     * @param row the row of the point
     * @param column the column of the point
     * @return whether the shot is valid or not
     */
    public static boolean validShot(int[] table, int row, int column)
    {
        if (row < 0 || row >= 10 || column < 0 || column >= 10)
            return false;
        if (table[Board.findNumericPlace(row, column, 10)] == MISSED || table[Board.findNumericPlace(row, column, 10)] == HIT)
            return false;
        return true;
    }

    /**
     * A valid shot (type 1) is a shot that the point is in the board and is empty or hit.
     * @param table the board to shoot at
     * @param row the row of the point
     * @param column the column of the point
     * @return whether the shot is valid or not
     */
    public static boolean validShot2(int[] table, int row, int column)
    {
        if (row < 0 || row >= 10 || column < 0 || column >= 10)
            return false;
        if (table[Board.findNumericPlace(row, column, 10)] == MISSED)
            return false;
        return true;
    }
}
