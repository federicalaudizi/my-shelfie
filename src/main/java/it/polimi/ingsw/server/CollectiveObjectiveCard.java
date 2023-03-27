package it.polimi.ingsw.server;
import java.util.Random;

/**
 * Abstract class for Collective Objective cards
 *
 * @author Federica
 */
public abstract class CollectiveObjectiveCard {
    public abstract boolean checkObjective(Shelf shelf);

    public int[] pickCard (){
        Random rand = new Random();
        int [] ObjectiveCards = {13,13};
        ObjectiveCards[0] = rand.nextInt(12) + 1;
        ObjectiveCards[1] = rand.nextInt() + 1;
        while (ObjectiveCards[0] != ObjectiveCards[1]){
            ObjectiveCards[1] = rand.nextInt() + 1;
        }

        return ObjectiveCards;
    }
}

