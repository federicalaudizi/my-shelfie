package it.polimi.ingsw.server.model;

import org.json.JSONObject;

/**
 * This class creates Point Cards.
 *
 * @author Sara Massarelli
 */
 public class PointCard {
   private int value;

    PointCard(int value){
       this.value= value;
   }

   PointCard(JSONObject pointCardJSON) {
       this.value = pointCardJSON.getInt("value");
   }

     int getValue() {
        return value;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("value", value);
        return obj;
    }

}

