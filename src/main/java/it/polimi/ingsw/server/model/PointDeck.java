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
    int size;

    PointDeck(int numOfPlayers) {
        cards = new Stack<>();

        if (numOfPlayers == 2) {
            cards.push(new PointCard(4));
            cards.push(new PointCard(8));
            size = 2;

        } else if (numOfPlayers == 3) {
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));
            size = 3;

        } else if (numOfPlayers == 4) {
            cards.push(new PointCard(2));
            cards.push(new PointCard(4));
            cards.push(new PointCard(6));
            cards.push(new PointCard(8));
            size = 4;

        }
    }

    /**
     * copy constructor
     */
    PointDeck(PointDeck other) {
        this.size = other.size;
        this.cards = new Stack<>();
        this.cards.addAll(other.cards);
    }

    /**
     * Creates this object from a JSONObject
     * @param pointDeckJson the JSONObject to copy from
     */
    PointDeck(JSONObject pointDeckJson){
        this.size = pointDeckJson.getInt("size");
        this.cards = new Stack<>();

        JSONArray cards = pointDeckJson.getJSONArray("cards");

        for(int i=0; i < cards.length(); i++){
            this.cards.push(new PointCard(cards.getJSONObject(i)));
        }
    }

    public boolean isEqualTo(PointDeck otherDeck) {
        if (this.size != otherDeck.size) {
            return false;
        } else {
            //creating temporary stacks
            Stack<PointCard> ts = new Stack<>();
            Stack<PointCard> tts = new Stack<>();

            ts.addAll(this.cards);
            tts.addAll(otherDeck.cards);

            while (!tts.empty()) {
                PointCard p = tts.pop();
                PointCard pp = ts.pop();

                if (!p.equals(pp))
                    return false;
            }
            return true;
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
        if (!cards.empty()) return cards.peek().getValue();
        else return 0;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("(");
        for (PointCard c : cards) {
            s.append(c.toString()).append(",");
        }
        return s + ")";
    }

    /**
     * @return this represented as a JSONObject
     */
    JSONObject toJson(){
        JSONObject ret = new JSONObject();

        ret.put("size", size);

        JSONArray stack = new JSONArray();
        for(PointCard card : cards){
            stack.put(card.toJson());
        }
        ret.put("cards", stack);

        return ret;
    }
}
