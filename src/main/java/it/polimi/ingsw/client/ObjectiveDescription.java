package it.polimi.ingsw.client;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

final class ObjectiveDescription {
    static final Map<String, String> init() {
        return Map.ofEntries(Map.entry("PatternOne", "Six groups each containing at least two tiles of the same type."), Map.entry("PatternTwo", "Four groups each containing al least 4 tiles of the same type."), Map.entry("PatternThree", "Four tiles of the same type in the four corners of the bookshelf."), Map.entry("PatternFour", "Two groups each containing 4 tiles of the same type in a 2x2 square."), Map.entry("PatternFive", "Three columns each formed by 6 tiles of maximum 3 different types."), Map.entry("PatternSix", "Eight tiles of the same type."), Map.entry("PatternSeven", "Five tiles of the same type forming a diagonal."), Map.entry("PatternEight", "Four lines each formed by 5 tiles of maximum three different types."), Map.entry("PatternNine", "Two columns each formed by 6 different types of tiles."), Map.entry("PatternTen", "Two lines each formed by 5 different types of tiles."), Map.entry("PatternEleven", "Five tiles of the same type forming an X."), Map.entry("PatternTwelve", "Five columns of increasing or decreasing height."));
    }

}
