package it.polimi.ingsw.server;

/**
 * Class that represents the player
 *
 * @author Federico
 */
public class Player {
    private Shelf playerShelf;
    private PersonalObjectiveCard objectiveCard;
    private PointCard[] pointCards;
    private boolean endGameCard;

    /**
     * Constructor of the class, assigns a clone of the PersonalObjectiveCard to the player
     *
     * @author Federico
     *
     * @param objectiveCard PersonalObjectiveCard to be assigned
     */
    Player(PersonalObjectiveCard objectiveCard){
        this.playerShelf = new Shelf();
        this.objectiveCard = new PersonalObjectiveCard(objectiveCard);
        this.pointCards = new PointCard[2];
        this.endGameCard = false;
    }

    /**
     * Returns a copy of the player shelf
     *
     * @author Federico
     *
     * @return copy of the player shelf
     */
    Shelf getShelf(){
        return new Shelf(playerShelf);
    }

    int calculatePoints(){
        //TODO: write the calculatePoints() method and JavaDoc

        return 0;
    }

    //TODO: calculate the assignPointCard() method, signature and JavaDoc
    //TODO: calculate the addPlayerTiles() method, signature and JavaDoc

}
