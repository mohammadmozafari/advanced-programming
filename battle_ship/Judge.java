/**
 * Handles the competition between 2 players.
 *
 * @author Mohammad Mozafari
 */
public class Judge
{
    Player[] players;

    /**
     * This constructors takes the players.
     * @param p1 player 1
     * @param p2 player 2
     */
    public Judge(Player p1, Player p2)
    {
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
    }

    /**
     * The game starts. The players take turn and make one shot at a turn.
     * The game ends when one player looses all his ships.
     */
    public void startGame()
    {
        players[0].setBoard();
        players[1].setBoard();

        int shot, result;
        for (int i = 0 ; ; i++)
        {
            if (players[0] instanceof Human && players[1] instanceof Human)
            {
                for (int j = 0; j < 100; j++)
                    System.out.println();
            }
            greet(players[i%2].getName(), ConsoleColors.PURPLE_BOLD);

            while (true)
            {
                shot = players[i % 2].move(players[(i + 1) % 2].getMap().getOpponentView());
                result = players[(i + 1) % 2].getMap().shoot(Board.findRow(shot, 10),
                        Board.findColumn(shot, 10), players[i % 2].isExactShot());

                if (result == 1)
                {
                    System.out.println(ConsoleColors.YELLOW_BOLD + players[i % 2].getName() + " missed" + ConsoleColors.RESET);
                    System.out.println();
                    break;
                }
                else
                {
                    System.out.println(ConsoleColors.GREEN_BOLD + "Wow, " + players[i % 2].getName()
                            + " took down a piece" + ConsoleColors.RESET);
                    System.out.println();
                    i++;
                    break;
                }

            }
            if (players[0].getMap().lost())
            {
                if (players[1] instanceof AI && players[0] instanceof Human) greet("YOU LOST", ConsoleColors.RED_BOLD);
                else greet(players[1].getName() + " WON", ConsoleColors.GREEN_BOLD);
                greet("FINAL RESULTS", ConsoleColors.WHITE_BOLD_BRIGHT);
                Board.printBoard(players[0].getMap().getPlaces(), 10, 10, false);
                Board.printBoard(players[1].getMap().getPlaces(), 10, 10, false);
                break;
            }
            else if (players[1].getMap().lost())
            {
                if (players[0] instanceof AI && players[1] instanceof Human) greet("YOU LOST", ConsoleColors.RED_BOLD);
                else greet(players[0].getName() + " WON", ConsoleColors.GREEN_BOLD);
                greet("FINAL RESULTS", ConsoleColors.WHITE_BOLD_BRIGHT);
                Board.printBoard(players[0].getMap().getPlaces(), 10, 10, false);
                Board.printBoard(players[1].getMap().getPlaces(), 10, 10, false);
                break;
            }
        }
    }

    /**
     * This static method takes a string and color and shows a greeting message in a particular frame.
     * @param name the message
     * @param color the color of the message to be printed
     */
    public static void greet(String name, String color)
    {
        System.out.println(color + "************** " + name + " **************");
        System.out.print("******************************");
        for (int i = 0 ; i < name.length() ; i++)
            System.out.print("*");
        System.out.println(ConsoleColors.RESET);
        System.out.println();
    }
}
