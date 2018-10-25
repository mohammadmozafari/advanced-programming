/**
 * This class holds the information about an animal it also implements the AnimalActions that define the behavior of an animal.
 *
 * @author Mohammad Mozafari
 */
public class Animal
{
    //fields
    private int energy, blood;
    private final int damage1, damage2, animalNumber;
    private final String name, damageName1, damageName2;

    private static final int[] firstDamages = new int[] {150, 130, 120, 100, 90, 70, 700, 80, 110, 90, 80, 200};
    private static final int[] secondDamages = new int[] {500, 600, 650, 0, 0, 50, 0, 0, 0, 100, 0, 0};
    private static final int[] energies = new int[] {1000, 900, 850, 600, 600, 500, 700, 500, 360, 400, 350, 230};
    private static final int[] bloods = new int[] {900, 850, 850, 350, 400, 1200, 450, 1100, 1000, 750, 200, 350};
    private static final String[] animalNames = new String[] {"lion", "bear", "tiger", "vulture", "fox", "elephant", "wolf", "wild boar","sea horse", "cow", "rabbit", "turtle"};
    private static final String[] firstDamagesNames = new String[] {"wound(150)", "wound(130)", "wound(120)", "wound(100)", "wound(90)", "hurt(70)", "kill(700)", "hurt(80)", "attack(110)", "attack(90)", "bite(80)", "bite(200)"};
    private static final String[] secondDamagesNames = new String[] {"kill(500)", "kill(600)", "kill(650)", "none(0)", "none(0)", "attack(50)", "none(0)", "none(0)", "none(0)", "wound(100)", "none(0)", "none(0)"};

    /**
     * This constructor sets the information of an animal based on the animal number
     * @param animalNumber a specific number that shows a specific animal
     */
    public Animal(int animalNumber)
    {
        this.animalNumber = animalNumber;
        name = animalNames[animalNumber];
        damageName1 = firstDamagesNames[animalNumber];
        damageName2 = secondDamagesNames[animalNumber];
        damage1 = firstDamages[animalNumber];
        damage2 = secondDamages[animalNumber];
        energy = energies[animalNumber];
        blood = bloods[animalNumber];
    }

    /**
     * This method is used when one or multiple animals attack another animal
     * @param attackers the attackers
     * @param attackChoices the types of attack
     * @param defender the defender
     * @return true if the attack is successful
     */
    public static boolean hurt(Animal[] attackers, int[] attackChoices, Animal defender)
    {
        int dmgSum = 0;
        int avgDamage;
        for (int i = 0 ; i < attackers.length ; i++)
            dmgSum += attackChoices[i] == 1 ? attackers[i].damage1 : attackers[i].damage2;
        avgDamage = dmgSum / attackers.length;
        for (int i = 0 ; i < attackers.length ; i++)
            if (attackers[i].getEnergy() < avgDamage)
                return false;
        for (int i = 0 ; i < attackers.length ; i++)
            attackers[i].setEnergy(attackers[i].energy - avgDamage);
        defender.setBlood(defender.blood - dmgSum);
        return true;
    }

    /**
     * This method sets the energy of the animal to the initial energy.
     */
    public void revive()
    {
        setEnergy(energies[this.getAnimalNumber()]);
    }

    //setters
    public void setBlood(int newBlood)
    {
        blood = newBlood;
    }
    public void setEnergy(int newEnergy)
    {
        energy = newEnergy;
    }

    //getters
    public int getDamage1()
    {
        return damage1;
    }
    public int getDamage2()
    {
        return damage2;
    }
    public int getBlood()
    {
        return blood;
    }
    public int getEnergy()
    {
        return energy;
    }
    public int getAnimalNumber()
    {
        return animalNumber;
    }
    public String getName()
    {
        return name;
    }
    public String getDamageName1()
    {
        return damageName1;
    }
    public String getDamageName2()
    {
        return damageName2;
    }

    /**
     * @return Comma Separated Values of the animal
     */
    @Override
    public String toString()
    {
        String result = animalNames[animalNumber] + "," + firstDamagesNames[animalNumber] + "," +
                secondDamagesNames[animalNumber] + "," + energy + "," + blood;
        return result;
    }

    /**
     * This method gives the initial energy of an animal.
     * @param animalNumber the animal number
     * @return the inital energy of that kind of animal
     */
    public static int getOriginalEnergy(int animalNumber)
    {
        return energies[animalNumber];
    }
}
