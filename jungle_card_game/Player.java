import java.util.ArrayList;

/**
 * This abstract class is the super class for all the players in the game.
 *
 * @author Mohammad Mozafari
 */
public abstract class Player
{
    //fields
    private int revivedTimes;
    private ArrayList<Animal> cards;
    private String name;

    public Player(String name)
    {
        cards = new ArrayList<>();
        this.name = name;
        revivedTimes = 0;
    }

    //abstract methods
    public abstract void move(Player p);
    public abstract void chooseCards(ArrayList<Animal> animals);

    /**
     * Checks if the player has lost the game or not
     * @return true if the player has no card left
     */
    public boolean lost()
    {
        getCards();
        return (cards.size() == 0);
    }

    /**
     * This method changes the energy of the player to its original amount
     * @param animal the animal to be revived
     * @return whether the revival was successful or not
     */
    protected boolean revive(Animal animal)
    {
        if (revivedTimes >= 3) return false;
        if (animal.getEnergy() == Animal.getOriginalEnergy(animal.getAnimalNumber())) return false;
        animal.revive();
        revivedTimes++;
        return true;
    }

    //getters
    public String getName()
    {
        return name;
    }
    protected ArrayList<Animal> getCards()
    {
        for (int i = 0 ; i < cards.size() ; i++)
        {
            if (cards.get(i).getBlood() <= 0)
            {
                cards.remove(i);
                i--;
            }
        }
        return cards;
    }

    /**
     * This method adds another card to the cards of the player.
     * @param animal the animal to be added
     */
    protected void addCard(Animal animal)
    {
        cards.add(animal);
    }

    /**
     * Checks to see if the player can make any move or not.
     * @return true if the player can has at least one move
     */
    public boolean hasMove()
    {
        for (int i = 0 ; i < cards.size() ; i++)
        {
            if (cards.get(i).getDamage2() == 0)
            {
                if (cards.get(i).getEnergy() >= cards.get(i).getDamage1())
                    return true;
            }
            else
            {
                if (cards.get(i).getEnergy() >= cards.get(i).getDamage1() || cards.get(i).getEnergy() >= cards.get(i).getDamage2())
                    return true;
            }
        }
        if (revivedTimes >= 3) return false;
        return true;
    }

    /**
     * Checks to prevent same animal multiple times
     * @param animals the animals
     * @param size the size of the array
     * @param animal the animal to be checked
     * @return true if the animal is already in the cards
     */
    protected boolean used(Animal[] animals, int size, Animal animal)
    {
        for (int i = 0 ; i < size ; i++)
        {
            if (animals[i] == animal)
                return true;
        }
        return false;
    }

    public int getRevivedTimes()
    {
        return revivedTimes;
    }
}
