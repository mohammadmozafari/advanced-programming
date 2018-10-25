import java.util.ArrayList;

/**
 * This holds the information about a human player and its behavior.
 *
 * @author Mohammad Mozafari
 */
public class Human extends Player
{
    /**
     * Sets the name of the player.
     * @param name the name of the player
     */
    public Human(String name)
    {
        super(name);
    }

    /**
     * A plyaer can do some operations such as single attack, double attack and etc.
     * This method handles these behaviors.
     * @param p the opponent
     */
    @Override
    public void move(Player p)
    {
        int choice;
        ArrayList<Animal> ownAnimals = super.getCards();
        ArrayList<Animal> opponentAnimals = p.getCards();

        while (true)
        {
            System.out.println("WHAT DO YOU WANT TO DO ?");
            System.out.println("1 -> ATTACK");
            System.out.println("2 -> HELP SOMEONE");
            System.out.println("3 -> YOUR CARDS");
            System.out.println("4 -> OPPONENT'S CARDS");
            choice = Main.getInt(1, 4);

            if (choice == 1)
            {
                if (!attack(ownAnimals, opponentAnimals))
                    Main.error("UNABLE TO ATTACK...");
                else break;
            }
            else if (choice == 2)
            {
                if (!revive(ownAnimals))
                    Main.error("UNABLE TO REVIVE THIS ANIMAL...");
                else break;
            }
            else if (choice == 3) Main.showAnimals(ownAnimals);
            else if (choice == 4) Main.showAnimals(opponentAnimals);
        }
    }

    /**
     * From the random cards given to each player, the player must choose 10 of them.
     * @param animals the random cards given to the player
     */
    @Override
    public void chooseCards(ArrayList<Animal> animals)
    {
        boolean flag = true;
        int[] numbers = new int[10];

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "-----------------------------------");
        System.out.println(super.getName());
        System.out.println("-----------------------------------" + ConsoleColors.RESET);

        Main.showAnimals(animals);
        System.out.println("ENTER THE NUMBER OF CARDS TO SELECT THEM, PRESS 'ENTER' AFTER EACH SELECTION");
        for (int i = 0 ; i < 10 ; i++)
        {
            numbers[i] = Main.getInt(1, 30) - 1;
            for (int j = 0 ; j < i ; j++)
                if (numbers[i] == numbers[j]) flag = false;

            if (flag == false)
            {
                i--;
                Main.error("A CARD CANNOT BE SELECTED MULTIPLE TIMES, TRY AGAIN");
            }
            flag = true;
        }

        for (int i = 0 ; i < 10 ; i++)
            super.addCard(animals.get(numbers[i]));
    }

    /**
     * This method is used when multiple animals attack an animal
     * @param ownAnimals the cards of the attacker
     * @param opponentAnimals the cards of the defender
     * @return true if the attack is successful
     */
    public boolean attack(ArrayList<Animal> ownAnimals, ArrayList<Animal> opponentAnimals)
    {
        boolean result = false;
        int num;
        int[] attackChoices;
        Animal temp, opponent;
        Animal[] attackers;

        System.out.println("HOW MANY ANIMALS ATTACK ? ");
        num = Main.getInt(1, ownAnimals.size());
        attackers = new Animal[num];
        attackChoices = new int[num];
        for (int i = 0 ; i < num ; i++)
        {
            System.out.println("ENTER THE NUMBER OF ANIMAL " + (i+1) + " :");
            temp = ownAnimals.get(Main.getInt(1, ownAnimals.size()) - 1);
            if (!used(attackers, i, temp))
            {
                attackers[i] = temp;
            }
            else
            {
                i--;
                continue;
            }
            if (attackers[i].getDamageName2().equals("none(0)"))
                attackChoices[i] = 1;
            else
            {
                System.out.println("HOW DO YOU WANT TO ATTACK ? ");
                System.out.println("1 -> " + attackers[i].getDamageName1());
                System.out.println("2 -> " + attackers[i].getDamageName2());
                attackChoices[i] = Main.getInt(1, 2);
            }
        }
        System.out.println("ENTER THE NUMBER OF ANIMAL YOU WANT TO ATTACK :");
        opponent = opponentAnimals.get(Main.getInt(1, opponentAnimals.size()) - 1);
        result = Animal.hurt(attackers, attackChoices, opponent);
        if (result)
            Main.logAttack(attackers, attackChoices, opponent);
        return result;
    }

    /**
     * This method gives an animal its full energy.
     * @param assests the own animals
     * @return whether revival was successful or not
     */
    public boolean revive(ArrayList<Animal> assests)
    {
        boolean result = false;
        Animal animal;

        System.out.println("ENTER THE NUMBER OF ANIMAL YOU WANT TO REVIVE");
        animal = assests.get(Main.getInt(1, assests.size()) - 1);
        result = super.revive(animal);
        if (result)
            Main.logRevival(animal);
        return result;
    }
}
