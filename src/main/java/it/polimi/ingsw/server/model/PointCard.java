package it.polimi.ingsw.server.model;

import org.json.JSONObject;

/**
 * This class creates Point Cards.
 *
 * @author Sara Massarelli
 */
public class PointCard {
    private final int value;

    PointCard(int value){
        this.value= value;
    }

    /**
     * Copy constructor
     * */
    PointCard(PointCard other){
        this.value = other.value;
    }

    /**
     * Constructs a PointCard object with the specified value.
     *
     * @param pointCardJSON A JSONObject containing the value of the PointCard.
     */
    PointCard(JSONObject pointCardJSON) {
        this.value = pointCardJSON.getInt("value");
    }


    /**
     * Retrieves the value of the PointCard.
     *
     * @return The value of the PointCard.
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts the PointCard object to a JSONObject.
     *
     * @return A JSONObject representation of the PointCard.
     */
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("value", value);
        return obj;
    }

    /**
     * Checks if the current PointCard is equal to another PointCard by comparing their values.
     *
     * @param other The PointCard object to compare against.
     * @return true if the PointCards have the same value, false otherwise.
     */
    public boolean equals(PointCard other){
        return this.value == other.value;
    }

    /**
     * Returns a string representation of the PointCard's value.
     *
     * @return The string representation of the PointCard's value.
     */
    public String toString(){
        return String.valueOf(value);
    }
}

