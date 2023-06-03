package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class that represents the player
 *
 * @author Federico
 */
public class Player {
    private Shelf playerShelf;
    private final PersonalObjectiveCard objectiveCard;
    private final PointCard[] pointCards;
    private boolean endGameCard;
    private String username;

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
        pointCards[0] = new PointCard(0);
        pointCards[1] = new PointCard(0);
        this.endGameCard = false;
    }

    /**
     * Gets the username of the player
     *
     * @return the username
     * @author Federica
     */
    public String getUsername(){
        return username;
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
        this.pointCards = new PointCard[2];
        for(int i = 0; i < 2; i++){
            if(toCopy.pointCards[i] == null) this.pointCards[i] = null;
            else this.pointCards[i] = new PointCard(toCopy.pointCards[i].getValue());
        }
        this.endGameCard = toCopy.endGameCard;
        this.username = toCopy.username;
    }

    /**
     * Constructor of the class, creates a player from a JSON object
     *
     * @author Federico
     *
     * @param playerJSON JSON object containing the player data
     */
    Player(JSONObject playerJSON){
        this.playerShelf = new Shelf(playerJSON.getJSONObject("playerShelf"));
        this.objectiveCard = new PersonalObjectiveCard(playerJSON.getJSONObject("objectiveCard"));
        this.pointCards = new PointCard[2];
        this.username = playerJSON.getString("username");
        for(int i = 0; i < 2; i++){
            this.pointCards[i] = new PointCard(playerJSON.getJSONArray("pointCards").getJSONObject(i));
        }
        this.endGameCard = playerJSON.getBoolean("endGameCard");
    }

    /**
     * Returns a copy of the player shelf
     *
     * @author Federico
     *
     * @return copy of the player shelf
     */
    public Shelf getShelf(){
        return new Shelf(playerShelf);
    }

    /**
     * Calculates the amount of points that the player has gaind until that point
     *
     * @return the amount of points
     * @author Federico
     */
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
     * Adds the givenCard to the point cards owned by the player, this method is deprecated and will be removed in the future
     *
     * @author Federico
     *
     * @param givenCard the card passed to the player
     */
    @Deprecated
    void assignPointCard(PointCard givenCard){
        if(pointCards[0].getValue() == 0){
            pointCards[0] = givenCard;
            return;
        }
        if(pointCards[1].getValue() == 0){
            pointCards[1] = givenCard;
        }
    }

    /**
     * Adds the givenCard to the point cards owned by the player
     *
     * @author Federico
     *
     * @param givenCard the card passed to the player
     * @param sourceDeck the deck from which the card was drawn
     */
    void assignPointCard(PointCard givenCard, int sourceDeck){
        pointCards[sourceDeck] = givenCard;
    }

    /**
     * Returns the status of the point cards owned by the player
     *
     * @author Federico
     *
     * @return 0 if the player has no point cards, 1 if the player has a point card from the first deck, 2 if the player has a point card from the second deck, 3 if the player all point cards
     */
    public int getPointCardStatus(){
        if(pointCards[0].getValue() == 0 && pointCards[1].getValue() == 0) return 0;
        else if(pointCards[0].getValue() != 0 && pointCards[1].getValue() == 0) return 1;
        else if(pointCards[0].getValue() == 0) return 2;
        else return 3;
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

    /**
     * Sets the player name
     */
    void setPlayerName(String username){
        this.username = username;
    }

    void setPlayerShelf(Shelf playerShelf){
        this.playerShelf = playerShelf;
    }


    @Override
    public String toString() {
        return "Player{\n" +
                "playerUsername={\n" + username +
                "}, \nplayerShelf={\n" + playerShelf +
                "},\nobjectiveCard={\n" + objectiveCard +
                "},\npointCards={[" + pointCards[0] +", "+pointCards[1]+"]"+
                "},\nendGameCard=" + endGameCard +
                "\n}";
    }

    /**
     * Checks if the given object is equal to this player
     *
     * @author Federico
     *
     * @param other the object to be compared
     * @return true if the given object is equal to this player, false otherwise
     */
    public boolean equals(Player other) {
        if (this == other) return true;
        if (other == null) return false;
        boolean pointCardCondition = true;

        for(int i=0; i<pointCards.length; i++){
            pointCardCondition = pointCards[i].equals(other.pointCards[i]);
        }

        boolean endGameCardCondition = endGameCard == other.endGameCard;
        boolean playerShelfCondition = playerShelf.equals(other.playerShelf);
        boolean objectiveCardCondition;
        objectiveCardCondition = objectiveCard.equals(other.objectiveCard);

        return endGameCardCondition &&
                playerShelfCondition &&
                objectiveCardCondition &&
                pointCardCondition;
    }

    /**
     * This method returns a representation of the player
     *
     * @return a JSON representing the player
     * @author Federica
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("playerShelf", playerShelf.toJson());
        jsonObject.put("objectiveCard", objectiveCard.toJson());
        JSONArray pointCardsJsonArray = new JSONArray();
        for (PointCard card : pointCards) {
            pointCardsJsonArray.put(card.toJson());
        }
        jsonObject.put("pointCards", pointCardsJsonArray);
        jsonObject.put("endGameCard", endGameCard);
        return jsonObject;
    }

    /**
     * Getter for the Personal Objective Card
     *
     * @return  the personal objective card of the player
     *
     * @author Sara
     * */
    public PersonalObjectiveCard getObjective(){
        return objectiveCard;
    }

    private void updateEndGameCard(boolean endGameCard) {
        this.endGameCard =  endGameCard;
    }

    public PointCard[] getPointCards(){
        return pointCards;
    }
}
