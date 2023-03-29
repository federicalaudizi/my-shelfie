package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static javax.swing.UIManager.put;

/**
 * Class that defines the Personal Objective Card
 *
 * @author Federico
 */
public class PersonalObjectiveCard{
    /**
     * Dictionary containing 6 elements, each element represent a tile type and is keyed by its coordinates on the shelf;
     */
    private HashMap<Coordinate, Tile> objective;

    /**
     * Array that contains already used patterns
     */
    private static ArrayList<PersonalObjectivePattern> usedPatterns = new ArrayList<PersonalObjectivePattern>();

    /**
     * Constructor of the personal objective, creates the objective choosing a random pattern from the PersonalObjectivePattern enum that hasn't been used yet
     *
     * @author Federico
     */
    PersonalObjectiveCard(){
        Random random = new Random();
        PersonalObjectivePattern pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        while(usedPatterns.contains(pattern)){
            pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        }
        usedPatterns.add(pattern);
        this.objective = pattern.getPattern();
    }

    /**
     * Copy constructor of the personal objective
     *
     * @param toCopy PersonalObjective that has to be copied
     */
    PersonalObjectiveCard(PersonalObjectiveCard toCopy){
        this.objective = new HashMap<Coordinate, Tile>();
        this.objective.putAll(toCopy.objective);
    }

    /**
     * Method that checks how many requirements of the objective are met
     *
     * @author Federico
     *
     * @param shelf The shelf that has to be checked
     * @return Returns the number of tiles in the shelf that matches the constraints given by the objective
     */
    int checkObjective(Shelf shelf){
        int correspondingTiles = 0;

        for(Coordinate checkingCoord : objective.keySet()){
            if(shelf.getTile(checkingCoord) == objective.get(checkingCoord)) correspondingTiles++;
        }

        return correspondingTiles;
    }

    /**
     * Enumeration that contains the possible patterns of the personal objective, each pattern is represented by an HashMap
     *
     * @author Federico
     */
    private enum PersonalObjectivePattern{
        FIRST_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(2, 5), Tile.TROFEI); //Cyan
            put(new Coordinate(4, 5), Tile.GATTI); //Green
            put(new Coordinate(3, 3), Tile.LIBRI); //White
            put(new Coordinate(1, 1), Tile.GIOCHI); //Yellow
            put(new Coordinate(3, 1), Tile.CORNICI); //Blue
            put(new Coordinate(0, 0), Tile.PIANTE); //Pink
        }}),

        SECOND_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(2, 5), Tile.TROFEI); //Cyan
            put(new Coordinate(4, 5), Tile.GATTI); //Green
            put(new Coordinate(3, 3), Tile.LIBRI); //White
            put(new Coordinate(1, 1), Tile.GIOCHI); //Yellow
            put(new Coordinate(3, 1), Tile.CORNICI); //Blue
            put(new Coordinate(0, 0), Tile.PIANTE); //Pink
        }}),
        //TODO: Implement all patterns
        ;
        private final HashMap<Coordinate, Tile> pattern;

        PersonalObjectivePattern(HashMap<Coordinate, Tile> pattern) {
            this.pattern = pattern;
        }

        public HashMap<Coordinate, Tile> getPattern() {
            return pattern;
        }
    }
}
