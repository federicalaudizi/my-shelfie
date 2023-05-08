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

    public int simplifiedPointDeck() {
        return this.topValue();
    }

    /**
     * copy constructor
     */
    PointDeck(PointDeck other) {
        this.size = other.size;
        this.cards = new Stack<>();
        Stack<PointCard> otherCards = new Stack<>();
        otherCards.addAll(other.cards);

        while (!otherCards.isEmpty()) {
            this.cards.push(otherCards.pop());
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

    /**
     * Json constructor
     * @param json is the json object containing the Point Deck
     * */
    public PointDeck (JSONObject json) {

        this.cards = new Stack<>();
        this.size = 0;
        JSONArray cardArray = json.getJSONArray("cards");
        for (int i = 0; i < cardArray.length(); i++) {
            JSONObject cardJson = cardArray.getJSONObject(i);
            PointCard card = new PointCard(cardJson);
            cards.push(card);
            this.size++;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder("(");
        for (PointCard c : cards) {
            s.append(c.toString()).append(",");
        }
        return s + ")";
    }
}
