package it.polimi.ingsw.server;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;

import java.util.ArrayList;

/**
 * Class that represents the player
 *
 * @author Federico
 */
public class Player {
    private final Shelf playerShelf;
    private final PersonalObjectiveCard objectiveCard;
    private final ArrayList<PointCard> pointCards;
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
        this.pointCards = new ArrayList<>();
        this.endGameCard = false;
    }

    /**
     * Copy constructor of the class
     *
     * @author Federico
     *
     * @param toCopy Player to be copied
     */
    Player(Player toCopy){
        this.playerShelf = new Shelf(toCopy.playerShelf);
        this.objectiveCard = new PersonalObjectiveCard(toCopy.objectiveCard);
        this.pointCards = new ArrayList<>(toCopy.pointCards);
        this.endGameCard = toCopy.endGameCard;
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
        int points = 0;

        //Adding points from endGameCard
        if(endGameCard) points += 1;

        //Adding points from the Personal objective
        points += objectiveCard.checkObjective(new Shelf(playerShelf));

        //Adding points from the earned point cards
        for(PointCard card : pointCards){
            points += card.getValue();
        }

        //Adding points from the shelf clusters
        points += playerShelf.getTileClusterPoints();

        return points;
    }

    /**
     * Adds the givenCard to the point cards owned by the player
     *
     * @param givenCard the card passed to the player
     */
    void assignPointCard(PointCard givenCard){
        pointCards.add(givenCard);
    }

    /**
     * Adds up to three tiles into the player's shelf in a specified column and in a specified order,
     * the first tile of the array gets placed in the lowest position of the selected column
     *
     * @author Federico
     *
     * @param column the number of the column where to place the tiles
     * @param tiles array containing the tiles in the intended placement order
     * @throws tooManyTilesException Exception thrown when the array is made of more than 3 tiles
     * @throws notEnoughTilesException Exception thrown when the array is empty
     * @throws fullColumnException Exception thrown when the selected column is full or there are not enough slots available
     */
    void addPlayerTiles(int column, Tile[] tiles) throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        playerShelf.addTiles(column, tiles);
    }

    /**
     * Marks this player as the first one to fill its shelf
     *
     * @author Federico
     */
    void setEndGameCard(){
        endGameCard = true;
    }

    @Override
    public String toString() {
        return "Player{\n" +
                "playerShelf={\n" + playerShelf +
                "},\nobjectiveCard={\n" + objectiveCard +
                "},\npointCards={\n" + pointCards +
                "},\nendGameCard=" + endGameCard +
                "\n}";
    }
}
