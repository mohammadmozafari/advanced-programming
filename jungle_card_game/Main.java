import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        int choice;
        Player[] players = new Player[2];
        Scanner in = new Scanner(System.in);
        String name;

        for (int i = 0 ; i < 2 ; i++)
        {
            System.out.println("PLAYER " + (i+1));
            System.out.println("1 - Human");
            System.out.println("2 - Artificial Intelligence");
            choice = getInt(1, 2);
            System.out.println("ENTER THE NAME OF THE PLAYER");
            name = in.nextLine();

            if (choice == 1)
                players[i] = new Human(name);
            else
                players[i] = new AI(name);
            System.out.println();
        }

        players[0].chooseCards(getRandomAnimals());
        System.out.println();
        players[1].chooseCards(getRandomAnimals());
        System.out.println();

        for (int i = 0 ; true ; i++)
        {
            System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "-----------------------------------");
            System.out.println(players[i % 2].getName());
            System.out.println("-----------------------------------" + ConsoleColors.RESET);
            if (!(players[0].hasMove() || players[1].hasMove()))
            {
                System.out.println("TIE");
                break;
            }
            if (!players[i % 2].hasMove())
            {
                error("THIS PLAYER DOESN'T HAVE ANY OPTION");
                continue;
            }
            players[i % 2].move(players[(i + 1) % 2]);
            if (players[0].lost() || players[1].lost())
                break;
        }

        if (players[0].lost())
            showGameResult(players[1].getName());
        else if (players[1].lost())
            showGameResult(players[0].getName());
    }

    /**
     * Shows an error message to the console with red color.
     * @param message the error message
     */
    public static void error(String message)
    {
        System.out.println(ConsoleColors.RED_BOLD_BRIGHT + message + ConsoleColors.RESET);
    }

    /**
     * This method takes a valid number between start and end.
     * The program doesn't crash if user enters sth but number.
     * @param start start of range
     * @param end end of range
     * @return a valid number
     */
    public static int getInt(int start, int end)
    {
        int number;
        Scanner in = new Scanner(System.in);

        while (true)
        {
            if (in.hasNextInt())
            {
                number = in.nextInt();
                in.nextLine();
                if (number >= start && number <= end)
                    return number;
                else
                    error("PLEASE ENTER A NUMBER BETWEEN " + start + " AND " + end);
            }
            else
            {
                in.nextLine();
                error("PLEASE ENTER A NUMBER");
            }
        }
    }

    /**
     * This method creates 30 random animals and there won't be more than 5 of any type.
     * @return 30 random animals
     */
    public static ArrayList<Animal> getRandomAnimals()
    {
        int choice;
        ArrayList<Animal> animals = new ArrayList<>();
        Random rand = new Random();


        for (int i = 0 ; i < 30 ; i++)
        {
            choice = rand.nextInt(12);
            if (checkAnimals(animals, choice))
                animals.add(new Animal(choice));
            else
                i--;
        }
        return animals;
    }

    /**
     * This method prints a deck of cards in a formatted way.
     * @param animals a deck of cards
     */
    public static void showAnimals(ArrayList<Animal> animals)
    {
        String[] split;
        System.out.println("-----------------------------------");
        System.out.printf(ConsoleColors.GREEN_BOLD_BRIGHT + "%-5s|%-20s|%-20s|%-20s|%-20s|%-20s" + ConsoleColors.RESET,
                "#", "Name", "Attack 1", "Attack 2", "Energy", "Blood");
        System.out.println();
        for (int i = 0 ; i < animals.size() ; i++)
        {
            System.out.printf("%-5d", (i+1));
            split = animals.get(i).toString().split(",");
            for (int j = 0 ; j < split.length ; j++)
                System.out.printf("|%-20s", split[j]);
            System.out.println();
        }
        System.out.println("-----------------------------------");
    }

    /**
     * Checks to ensure that there isn't more than 5 animals of the same kind in the cards
     * @param animals the deck of cards
     * @param animalNumber the animal to be checked
     * @return the result of the check
     */
    private static boolean checkAnimals(ArrayList<Animal> animals, int animalNumber)
    {
        int counter = 0;
        for (Animal a : animals)
        {
            if (a.getAnimalNumber() == animalNumber)
                counter++;
        }
        return (counter < 5);
    }

    public static void logAttack(Animal[] attackers, int[] attackType, Animal defender)
    {
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT);
        System.out.print("Attackers : ");
        for (int i = 0 ; i < attackers.length ; i++)
        {
            System.out.print(attackers[i].getName());
            if (attackType[i] == 1)
                System.out.print("(" + attackers[i].getDamage1() + "),");
            else
                System.out.print("(" + attackers[i].getDamage2() + "),");
        }
        System.out.println();
        System.out.println("Defender : " + defender.getName());
        System.out.println(ConsoleColors.RESET);
    }

    /**
     * This log prints the information about a revival in the console
     * @param animal the animal tha is revived
     */
    public static void logRevival(Animal animal)
    {
        System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT);
        System.out.println("Revival");
        System.out.println("Current Energy : " + animal.getEnergy());
        System.out.println(ConsoleColors.RESET);
    }

    /**
     * This method shows the final message about the game results.
     * @param winner the name of the winner
     */
    private static void showGameResult(String winner)
    {
        System.out.println();
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println(winner + " IS THE WINNER");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------" + ConsoleColors.RESET);
    }
}
