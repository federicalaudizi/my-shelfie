package it.polimi.ingsw.server.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Stack;

/**
 * This class creates Point Decks for Common Objective.
 * It needs the number of Player to decide how many and which cards to put in the stack.
 *
 * @author Sara Massarelli
 */

public class PointDeck {
    Stack<PointCard> cards;

    PointDeck(int numOfPlayers) {
        cards = new Stack<>();

        if (numOfPlayers == 2) {
            cards.push(new PointCard(4));
            cards.push(new PointCard(8));

        } else if (numOfPlayers == 3) {
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));

        } else if (numOfPlayers == 4) {
            cards.push(new PointCard(2));
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));

        }
    }

    /**
     * @return the card with maximum disposable points for that common objective card
     */
    PointCard takePoints() {
        return cards.pop();
    }

    /**
     * @return the value of the card on top of the deck
     */
    int topValue() {
        if(!cards.empty()) return cards.peek().getValue();
        else return 0;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray cardArray = new JSONArray();

        for (PointCard card : cards) {
            JSONObject cardJson = card.toJson();
            cardArray.put(cardJson);
        }

        json.put("cards", cardArray);
        return json;
    }

}
