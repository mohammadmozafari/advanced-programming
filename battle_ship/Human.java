import java.util.Scanner;

/**
 * This class holds the information about a human player.
 * It enables the player to set the board and make a move.
 * @author Mohammad Mozafari
 */
public class Human extends Player
{
    /**
     * This constructor sets the information of a human player.
     * @param exactShot whether the shot is accurate or not
     * @param name the name of the player
     */
    public Human(boolean exactShot, String name)
    {
        super(exactShot, name);
    }

    /**
     * This method enables the player to watch its status and the opponent's status and make a shot.
     * @param table the opponent table
     */
    @Override
    public int move(int[] table)
    {
        int choice, row, column;
        Scanner in = new Scanner(System.in);
        super.increaseMoves();

        while (true)
        {
            showMenu();
            choice = Main.getInt();
            if (choice != 1 && choice != 2 && choice != 3)
            {
                System.out.println("THE NUMBER SHOULD BE 1 OR 2 OR 3");
                continue;
            }

            if (choice == 1)
            {
                System.out.println("SHOT WHERE ?");
                System.out.print("ROW : ");
                row = Main.getInt();
                System.out.print("COLUMN : ");
                column = Main.getInt();

                if (Board.validShot(table, row, column)) return Board.findNumericPlace(row, column, 10);
                System.out.println("NOT A VALID SHOT, TRY AGAIN");
            }
            else if (choice == 2)
            {
                Board.printBoard(super.getMap().getPlaces(), 10, 10, false);
            }
            else if (choice == 3)
            {
                Board.printBoard(table, 10, 10, true);
            }
            else continue;
        }
    }

    /**
     * Fills the board with 5 ships and checks whether the positioning is valid or not.
     */
    @Override
    public void setBoard()
    {
        boolean vertical;
        int row, column, length;
        Scanner in = new Scanner(System.in);

        System.out.println(ConsoleColors.WHITE_BOLD + "HI, IN THIS PART YOU SET YOUR BOARD WITH SHIPS");
        System.out.println("YOU HAVE 5 SHIPS AND THE SHIPS CAN HAVE THE LENGTH IN RANGE OF 2 TO 5");
        System.out.println("--------------------------------------" + ConsoleColors.RESET);

        for (int i = 0 ; i < 5 ; i++)
        {
            Judge.greet(super.getName(), ConsoleColors.PURPLE_BOLD);
            System.out.println("SHIP NUMBER " + (i+1));

            while (true)
            {
                System.out.println("ENTER SHIP INFO");
                System.out.println("STARTING POINT");
                System.out.print("ROW : ");
                row = Main.getInt();
                System.out.print("COLUMN : ");
                column = Main.getInt();
                System.out.print("LENGTH : ");
                length = Main.getInt();

                System.out.println("IF THE SHIP IS VERTICAL ENTER 'true' OTHER WISE ENTER 'false'");
                vertical = Main.getBoolean();

                System.out.println("--------------------------------------");
                if (super.getMap().addShip(row, column, length, vertical))
                    break;
                System.out.println("SOMETHING WENT WRONG, TRY AGAIN...");
            }

        }
    }

    /*
    Shows a menu to the player to choose.
     */
    private void showMenu()
    {
        System.out.println("1 - MAKE A SHOT");
        System.out.println("2 - YOUR MAP");
        System.out.println("3 - OPPONENT'S MAP");
    }
}
