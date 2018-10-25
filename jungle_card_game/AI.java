import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * This method holds a AI player and also defines its behavior.
 *
 * @author Mohammad Mozafari
 */
public class AI extends Player
{
    public AI(String name)
    {
        super(name);
    }

    /**
     * This method is used when the computer wants to make a move in the game
     * @param p the opponent player
     */
    @Override
    public void move(Player p)
    {
        int minDamage = 0, maxDamage = 0, optionNumber = 0, attackType = 0, numOfAnimals;
        int[] dmg = new int[4];
        int[] multiAttackerChoice;
        Animal defender, tAttack, tDefend, tAttack2, temp;
        Animal[] attacker = new Animal[1];
        Animal[] attackers = new Animal[2];
        Animal[] multiAttackers;
        ArrayList<Animal> opponentAnimals = p.getCards();
        ArrayList<Animal> ownAnimals = super.getCards();
        Comparator<Animal> energySort;
        Random rand = new Random();

        attackers[0] = attackers[1] = defender = null;

        energySort = (Animal a, Animal b) ->
        {
            if (Animal.getOriginalEnergy(a.getAnimalNumber()) - a.getEnergy() < Animal.getOriginalEnergy(b.getAnimalNumber()) - b.getBlood()) return 1;
            else if (Animal.getOriginalEnergy(a.getAnimalNumber()) - a.getEnergy() ==
                    Animal.getOriginalEnergy(b.getAnimalNumber()) - b.getBlood())
                    return 0;
            else return -1;
        };

        for (int i = 0 ; i < opponentAnimals.size() ; i++)
        {
            for (int j = 0 ; j < ownAnimals.size() ; j++)
            {
                tAttack = ownAnimals.get(j);
                tDefend = opponentAnimals.get(i);
                dmg[0] = tAttack.getDamage1();
                dmg[1] = tAttack.getDamage2();

                for (int k = 0 ; k < 2 ; k++)
                {
                    if (dmg[k] >= tDefend.getBlood() && tAttack.getEnergy() >= dmg[k])
                    {
                        if (optionNumber == 0 || dmg[k] < minDamage)
                        {
                            minDamage = dmg[k];
                            attacker[0] = ownAnimals.get(j);
                            defender = opponentAnimals.get(i);
                            attackType = k+1;
                            optionNumber++;
                        }
                    }
                }
                for (int k = j + 1 ; k < ownAnimals.size() ; k++)
                {
                    tAttack2 = ownAnimals.get(k);
                    dmg[0] = tAttack.getDamage1() + tAttack2.getDamage1();
                    dmg[1] = tAttack.getDamage1() + tAttack2.getDamage2();
                    dmg[2] = tAttack.getDamage2() + tAttack2.getDamage1();
                    dmg[3] = tAttack.getDamage2() + tAttack2.getDamage2();

                    for (int l = 0 ; l < 4 ; l++)
                    {
                        if (dmg[l] >= tDefend.getBlood() && tAttack.getEnergy() >= dmg[l] / 2 && tAttack2.getEnergy() >= dmg[l] / 2)
                        {
                            if (optionNumber == 0 || dmg[l] < minDamage)
                            {
                                minDamage = dmg[l];
                                attackers[0] = tAttack;
                                attackers[1] = tAttack2;
                                defender = tDefend;
                                attackType = l + 3;
                                optionNumber++;
                            }
                        }
                    }
                }
            }
        }

        if (attackType == 1)
        {
            Animal.hurt(attacker, new int[] {1}, defender);
            Main.logAttack(attacker, new int[] {1}, defender);
        }
        else if (attackType == 2)
        {
            Animal.hurt(attacker, new int[]{2}, defender);
            Main.logAttack(attacker, new int[]{2}, defender);
        }
        else if (attackType == 3)
        {
            Animal.hurt(attackers, new int[]{1, 1}, defender);
            Main.logAttack(attackers, new int[]{1, 1}, defender);
        }
        else if (attackType == 4)
        {
            Animal.hurt(attackers, new int[]{1, 2}, defender);
            Main.logAttack(attackers, new int[]{1, 2}, defender);
        }
        else if (attackType == 5)
        {
            Animal.hurt(attackers, new int[]{2, 1}, defender);
            Main.logAttack(attackers, new int[]{2, 1}, defender);
        }
        else if (attackType == 6)
        {
            Animal.hurt(attackers, new int[]{2, 2}, defender);
            Main.logAttack(attackers, new int[]{2, 2}, defender);
        }
        if (attackType != 0) return;


        ownAnimals.sort(energySort);

        for (int i = 0 ; i < ownAnimals.size() ; i++)
        {
            if (getRevivedTimes() >= 3) break;
            else if (revive(ownAnimals.get(i)))
            {
                Main.logRevival(ownAnimals.get(0));
                return;
            }
        }
        for (int counter = 0 ; true ; counter++)
        {
            defender = opponentAnimals.get(rand.nextInt(opponentAnimals.size()));
            if (counter >= 10)
                numOfAnimals = 1;
            else numOfAnimals = getRandomAnimals(ownAnimals.size());
            multiAttackers = new Animal[numOfAnimals];
            multiAttackerChoice = new int[numOfAnimals];
            for (int i = 0; i < numOfAnimals; i++)
            {
                temp = ownAnimals.get(rand.nextInt(ownAnimals.size()));
                if (!used(multiAttackers, i, temp))
                    multiAttackers[i] = temp;
                else
                {
                    i--;
                    continue;
                }
                if (multiAttackers[i].getDamageName2().equals("none(0)"))
                    multiAttackerChoice[i] = 1;
                else
                    multiAttackerChoice[i] = rand.nextInt(2) + 1;

            }
            if (Animal.hurt(multiAttackers, multiAttackerChoice, defender))
            {
                Main.logAttack(multiAttackers, multiAttackerChoice, defender);
                return;
            }
        }
    }

    /**
     * With this method computer chooses 10 cards from the 30 random given cards
     * @param animals 30 random give cards
     */
    @Override
    public void chooseCards(ArrayList<Animal> animals)
    {
        boolean repeat = false;
        ArrayList<Animal> chosen = new ArrayList<>();
        Main.showAnimals(animals);

        Comparator<Animal> attackBased = (Animal a, Animal b) ->
        {
            int x = a.getDamage1()+a.getDamage2();
            int y = b.getDamage1()+b.getDamage2();

            if (x > y) return -1;
            else if (x == y) return 0;
            else return 1;
        };
        Comparator<Animal> energyAndBlood = (Animal a, Animal b) ->
        {
            int x = a.getBlood() + a.getEnergy();
            int y = b.getBlood() + b.getEnergy();

            if (y > x) return 1;
            else if (x == y) return 0;
            else return -1;
        };

        animals.sort(attackBased);
        for (int i = 0 ; i < 5 ; i++)
            chosen.add(animals.get(i));

        animals.sort(energyAndBlood);
        for (int i = 0 ; ; i++)
        {
            repeat = false;
            for (Animal a : chosen)
                if (a == animals.get(i))
                    repeat = true;
            if (!repeat)
                chosen.add(animals.get(i));
            if (chosen.size() == 10) break;
        }

        for (Animal a : chosen)
            super.addCard(a);
    }

    /**
     * This method gives a random number with logarithmic probability for the attackers number
     * @param numOfAnimals the number of animals
     * @return the number of attackers
     */
    private int getRandomAnimals(int numOfAnimals)
    {
        int randomNumber;
        double randomRange = 1000;
        Random rand = new Random();

        double[] partitions = new double[numOfAnimals];
        for (int i = 0 ; i < numOfAnimals ; i++)
        {
            partitions[i] = 1000 - randomRange;
            randomRange /= 2;
        }
        randomNumber = rand.nextInt(1000)+1;
        for (int i = 0 ; i < numOfAnimals-1 ; i++)
        {
            if (randomNumber >= partitions[i] && randomNumber <= partitions[i+1])
                return (i+1);
        }
        return numOfAnimals;
    }
}
