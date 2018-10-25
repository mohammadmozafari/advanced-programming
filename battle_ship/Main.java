import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        boolean exact;
        int choice;
        Player[] players = new Player[2];
        Scanner in = new Scanner(System.in);
        String name;

        for (int i = 0 ; i < 2 ; i++)
        {
            System.out.println("PLAYER " + (i + 1));
            System.out.println("1 - HUMAN");
            System.out.println("2 - AI");

            while (true)
            {
                choice = getInt();
                if (choice == 1 || choice == 2) break;
                System.out.println("THE NUMBER MUST BE 1 OR 2");
            }

            System.out.print("NAME : ");
            name = in.nextLine();

            System.out.println("EXACT SHOT ? (ONLY ENTER 'true' OR 'false')");
            exact = getBoolean();

            if (choice == 1)
                players[i] = new Human(exact, name);
            else
                players[i] = new AI(exact, name);

        }
        Judge j = new Judge(players[0], players[1]);
        j.startGame();
    }

    /**
     * Takes an integer from input.
     * If the user enters something else it asks again.
     * @return the integer
     */
    public static int getInt()
    {
        int number;
        Scanner in = new Scanner(System.in);

        while (true)
        {
            if (in.hasNextInt())
            {
                number = in.nextInt();
                in.nextLine();
                break;
            }
            else
            {
                System.out.println("YOU MUST ENTER AN INTEGER, TRY AGAIN");
                in.nextLine();
            }
        }
        return number;
    }

    /**
     * Gets a boolean from input.
     * If user enters something else it asks again.
     * @return the boolean value
     */
    public static boolean getBoolean()
    {
        boolean bool;
        Scanner in = new Scanner(System.in);

        while (true)
        {
            if (in.hasNextBoolean())
            {
                bool = in.nextBoolean();
                in.nextLine();
                break;
            }
            else
            {
                System.out.println("YOU MUST ENTER A BOOLEAN, TRY AGAIN");
                in.nextLine();
            }
        }
        return bool;
    }
}
