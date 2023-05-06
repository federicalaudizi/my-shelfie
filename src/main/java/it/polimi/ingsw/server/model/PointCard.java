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

    public static PointCard fromJson(String json){
       JSONObject obj = new JSONObject();
       int value = obj.getInt(json);
       return new PointCard(value);
    }

    public boolean equals(PointCard other){
        return this.value == other.value;
    }

    public String toString(){
        return String.valueOf(value);
    }
}

