package it.polimi.ingsw.server;

import java.util.Stack;
/**
 * This class creates Point Decks for Common Objective.
 * It needs the number of Player to decide how many and which cards to put in the stack.
 *
 * @author Sara Massarelli
 */

public class PointDeck {
     Stack<PointCard> cards;

     PointDeck(int numOfPlayers){
        cards = new Stack<>();

        if(numOfPlayers == 2){
            cards.push(new PointCard(4));
            cards.push(new PointCard(8));

        } else if (numOfPlayers==3) {
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));

        } else if (numOfPlayers==4) {
            cards.push(new PointCard(2));
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));

        }
    }
    /**
     * @return the card with maximum disposable points for that common objective card
     *
     */
    PointCard takePoints(){
        return cards.pop();
    }
}
