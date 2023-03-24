package it.polimi.ingsw.server;

import java.util.ArrayList;
/**
 * This class creates Point Decks for Common Objective
 *
 * @author Sara Massarelli
 */

public class PointDeck {
    private ArrayList<PointCard> cards;

    public PointDeck(int numOfPlayers){
        cards = new ArrayList<>();

        if(numOfPlayers == 2){
            cards.add(new PointCard(4));
            cards.add(new PointCard(8));

        } else if (numOfPlayers==3) {
            cards.add(new PointCard(4));
            cards.add(new PointCard(6));
            cards.add(new PointCard(8));

        } else if (numOfPlayers==4) {
            cards.add(new PointCard(2));
            cards.add(new PointCard(4));
            cards.add(new PointCard(6));
            cards.add(new PointCard(8));

        }
    }
}
