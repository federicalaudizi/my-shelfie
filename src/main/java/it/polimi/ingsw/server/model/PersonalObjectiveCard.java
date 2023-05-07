package it.polimi.ingsw.server.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Class that defines the Personal Objective Card
 *
 * @author Federico
 */
public class PersonalObjectiveCard {
    /**
     * Dictionary containing 6 elements, each element represent a tile type and is keyed by its coordinates on the shelf;
     */
    private final PersonalObjectivePattern objective;

    /**
     * HashMap that contains already used patterns
     */

    public static final HashMap<Game, List<PersonalObjectivePattern>> usedPattern = new HashMap<>();

    /**
     * Constructor of the personal objective, creates the objective choosing a random pattern from the PersonalObjectivePattern enum that hasn't been used yet
     *
     * @param game in which PersonalObjectiveCards are used
     * @throws IllegalStateException when all personal objective cards have been handled, this exception should be impossible to reach
     * @author Federico, Sara
     */
    PersonalObjectiveCard(Game game) throws IllegalStateException {
        List<PersonalObjectivePattern> patterns = usedPattern.getOrDefault(game, new ArrayList<>());
        if (patterns.size() == PersonalObjectivePattern.values().length) {
            throw new IllegalStateException("All the personal objectives have been used for this game");
        }
        Random random = new Random();
        PersonalObjectivePattern pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        while (patterns.contains(pattern)) {
            pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        }
        patterns.add(pattern);
        usedPattern.put(game, patterns);
        this.objective = pattern;
    }


    /**
     * Constructor of the personal objective, creates the objective from a JSONObject
     *
     * @param object JSONObject that contains the personal objective
     * @author Federico
     */
    PersonalObjectiveCard(JSONObject object) {
        int pattern = object.getInt("code");
        this.objective = PersonalObjectivePattern.values()[pattern - 1];
    }

    /**
     * Copy constructor of the personal objective
     *
     * @param toCopy PersonalObjective that has to be copied
     */
    PersonalObjectiveCard(PersonalObjectiveCard toCopy) {
        this.objective = toCopy.objective;
    }

    /**
     * Constructor of the personal objective, creates the objective with a pattern passed as parameter, this constructor is deprecated because it is used only for testing purposes
     *
     * @param pattern Pattern that has to be used
     * @author Federic0
     */
    @Deprecated
    PersonalObjectiveCard(PersonalObjectivePattern pattern) {
        this.objective = pattern;
    }

    /**
     * Method that checks how many points the player has earned with the personal objective
     *
     * @param shelf The shelf that has to be checked
     * @return Returns how many points the player has earned with the personal objective
     * @author Federico
     */
    int checkObjective(Shelf shelf) {
        int correspondingTiles = 0;

        for (Coordinate checkingCoord : objective.getPattern().keySet()) {
            if (shelf.getTile(checkingCoord) == objective.getPattern().get(checkingCoord)) correspondingTiles++;
        }

        if (correspondingTiles == 0) return 0;
        else if (correspondingTiles == 1) return 1;
        else if (correspondingTiles == 2) return 2;
        else if (correspondingTiles == 3) return 4;
        else if (correspondingTiles == 4) return 6;
        else if (correspondingTiles == 5) return 9;
        else if (correspondingTiles == 6) return 12;
        else throw new IllegalStateException("Something went wrong while checking the personal objective");
    }

    /**
     * Method that returns the pattern of the personal objective
     *
     * @return Returns the pattern of the personal objective
     * @author Federico
     */
    PersonalObjectivePattern getPattern() {
        return objective;
    }

    public String toString() {
        return objective.toString();
    }

    /**
     * This method returns a representation of the PersonalObjectiveCard
     *
     * @return a JSON representing the PersonalObjectiveCard
     * @author Federica, Federico
     */
    public JSONObject toJson() {
        JSONObject me = new JSONObject();
        switch (objective) {
            case FIRST_PATTERN -> me.put("code", 1);
            case SECOND_PATTERN -> me.put("code", 2);
            case THIRD_PATTERN -> me.put("code", 3);
            case FOURTH_PATTERN -> me.put("code", 4);
            case FIFTH_PATTERN -> me.put("code", 5);
            case SIXTH_PATTERN -> me.put("code", 6);
            case SEVENTH_PATTERN -> me.put("code", 7);
            case EIGHTH_PATTERN -> me.put("code", 8);
            case NINTH_PATTERN -> me.put("code", 9);
            case TENTH_PATTERN -> me.put("code", 10);
            case ELEVENTH_PATTERN -> me.put("code", 11);
            case TWELFTH_PATTERN -> me.put("code", 12);
        }
        me.put("pattern", objective.toString());
        return me;
    }

    /**
     * This method compares this to another
     *
     * @param other the object to compare to
     * @return true if equals, false otherwise
     * @author Federico
     */
    public boolean equals(PersonalObjectiveCard other) {
        return PersonalObjectivePattern.valueOf(objective.name()).equals(PersonalObjectivePattern.valueOf(other.objective.name()));
    }

    /**
     * Enumeration that contains the possible patterns of the personal objective, each pattern is represented by an HashMap
     *
     * @author Federico
     */
    enum PersonalObjectivePattern {
        FIRST_PATTERN(new HashMap<>() {{
            put(new Coordinate(2, 5), Tile.TROPHIES); //Cyan
            put(new Coordinate(4, 5), Tile.CATS); //Green
            put(new Coordinate(3, 3), Tile.BOOKS); //White
            put(new Coordinate(1, 1), Tile.GAMES); //Yellow
            put(new Coordinate(3, 1), Tile.FRAMES); //Blue
            put(new Coordinate(0, 0), Tile.PLANTS); //Pink
        }}),

        SECOND_PATTERN(new HashMap<>() {{
            put(new Coordinate(1, 4), Tile.TROPHIES); //Cyan
            put(new Coordinate(3, 0), Tile.CATS); //Green
            put(new Coordinate(2, 2), Tile.BOOKS); //White
            put(new Coordinate(0, 0), Tile.GAMES); //Yellow
            put(new Coordinate(1, 2), Tile.FRAMES); //Blue
            put(new Coordinate(1, 4), Tile.PLANTS); //Pink
        }}),

        THIRD_PATTERN(new HashMap<>() {{
            put(new Coordinate(0, 2), Tile.TROPHIES); //Cyan
            put(new Coordinate(0, 5), Tile.CATS); //Green
            put(new Coordinate(2, 0), Tile.BOOKS); //White
            put(new Coordinate(4, 1), Tile.GAMES); //Yellow
            put(new Coordinate(3, 4), Tile.FRAMES); //Blue
            put(new Coordinate(1, 3), Tile.PLANTS); //Pink
        }}),

        FOURTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(3, 0), Tile.TROPHIES); //Cyan
            put(new Coordinate(4, 1), Tile.CATS); //Green
            put(new Coordinate(1, 4), Tile.BOOKS); //White
            put(new Coordinate(0, 3), Tile.GAMES); //Yellow
            put(new Coordinate(2, 2), Tile.FRAMES); //Blue
            put(new Coordinate(2, 5), Tile.PLANTS); //Pink
        }}),

        FIFTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(0, 3), Tile.TROPHIES); //Cyan
            put(new Coordinate(2, 1), Tile.CATS); //Green
            put(new Coordinate(1, 1), Tile.BOOKS); //White
            put(new Coordinate(4, 5), Tile.GAMES); //Yellow
            put(new Coordinate(2, 3), Tile.FRAMES); //Blue
            put(new Coordinate(3, 2), Tile.PLANTS); //Pink
        }}),

        SIXTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(1, 1), Tile.TROPHIES); //Cyan
            put(new Coordinate(2, 3), Tile.CATS); //Green
            put(new Coordinate(4, 2), Tile.BOOKS); //White
            put(new Coordinate(2, 5), Tile.GAMES); //Yellow
            put(new Coordinate(0, 0), Tile.FRAMES); //Blue
            put(new Coordinate(4, 1), Tile.PLANTS); //Pink
        }}),

        SEVENTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(2, 0), Tile.TROPHIES); //Cyan
            put(new Coordinate(4, 4), Tile.CATS); //Green
            put(new Coordinate(3, 3), Tile.BOOKS); //White
            put(new Coordinate(1, 2), Tile.GAMES); //Yellow
            put(new Coordinate(2, 5), Tile.FRAMES); //Blue
            put(new Coordinate(0, 5), Tile.PLANTS); //Pink
        }}),

        EIGHTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(3, 1), Tile.TROPHIES); //Cyan
            put(new Coordinate(0, 3), Tile.CATS); //Green
            put(new Coordinate(4, 2), Tile.BOOKS); //White
            put(new Coordinate(2, 3), Tile.GAMES); //Yellow
            put(new Coordinate(4, 0), Tile.FRAMES); //Blue
            put(new Coordinate(1, 4), Tile.PLANTS); //Pink
        }}),

        NINTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(2, 3), Tile.TROPHIES); //Cyan
            put(new Coordinate(1, 4), Tile.CATS); //Green
            put(new Coordinate(3, 1), Tile.BOOKS); //White
            put(new Coordinate(3, 0), Tile.GAMES); //Yellow
            put(new Coordinate(4, 5), Tile.FRAMES); //Blue
            put(new Coordinate(0, 2), Tile.PLANTS); //Pink
        }}),

        TENTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(4, 5), Tile.TROPHIES); //Cyan
            put(new Coordinate(3, 2), Tile.CATS); //Green
            put(new Coordinate(0, 3), Tile.BOOKS); //White
            put(new Coordinate(1, 4), Tile.GAMES); //Yellow
            put(new Coordinate(1, 1), Tile.FRAMES); //Blue
            put(new Coordinate(0, 3), Tile.PLANTS); //Pink
        }}),

        ELEVENTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(3, 2), Tile.TROPHIES); //Cyan
            put(new Coordinate(0, 0), Tile.CATS); //Green
            put(new Coordinate(2, 5), Tile.BOOKS); //White
            put(new Coordinate(4, 1), Tile.GAMES); //Yellow
            put(new Coordinate(2, 3), Tile.FRAMES); //Blue
            put(new Coordinate(1, 4), Tile.PLANTS); //Pink
        }}),

        TWELFTH_PATTERN(new HashMap<>() {{
            put(new Coordinate(4, 2), Tile.TROPHIES); //Cyan
            put(new Coordinate(1, 2), Tile.CATS); //Green
            put(new Coordinate(0, 0), Tile.BOOKS); //White
            put(new Coordinate(3, 4), Tile.GAMES); //Yellow
            put(new Coordinate(0, 4), Tile.FRAMES); //Blue
            put(new Coordinate(2, 3), Tile.PLANTS); //Pink
        }}),
        ;
        private final HashMap<Coordinate, Tile> pattern;

        PersonalObjectivePattern(HashMap<Coordinate, Tile> pattern) {
            this.pattern = pattern;
        }

        public HashMap<Coordinate, Tile> getPattern() {
            return pattern;
        }

        public String toString() {
            StringBuilder ret = new StringBuilder();

            for (int j = 5; j >= 0; j--) {
                for (int i = 0; i < 5; i++) {
                    Coordinate printingCoord = null;
                    for (Coordinate checkingCoord : pattern.keySet()) {
                        if (checkingCoord.getRow() == i && checkingCoord.getColumn() == j) {
                            printingCoord = checkingCoord;
                        }
                    }

                    if (printingCoord != null) {
                        ret.append(pattern.get(printingCoord).getSymbol()).append(" ");
                    } else {
                        ret.append("e ");
                    }
                }
            }

            return ret.toString();
        }
    }
}
