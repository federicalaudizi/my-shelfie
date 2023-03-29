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
    private final PersonalObjectivePattern objective;

    /**
     * Array that contains already used patterns
     */
    private static final ArrayList<PersonalObjectivePattern> usedPatterns = new ArrayList<PersonalObjectivePattern>();

    /**
     * Constructor of the personal objective, creates the objective choosing a random pattern from the PersonalObjectivePattern enum that hasn't been used yet
     *
     * @author Federico
     *
     * @throws IllegalStateException when all personal objective cards have been handled, this exception should be impossible to reach
     */
    PersonalObjectiveCard() throws IllegalStateException{
        if(usedPatterns.size() == PersonalObjectivePattern.values().length) throw new IllegalStateException("All the personal objectives have been used");
        Random random = new Random();
        PersonalObjectivePattern pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        while(usedPatterns.contains(pattern)){
            pattern = PersonalObjectivePattern.values()[random.nextInt(PersonalObjectivePattern.values().length)];
        }
        usedPatterns.add(pattern);
        this.objective = pattern;
    }

    /**
     * Copy constructor of the personal objective
     *
     * @param toCopy PersonalObjective that has to be copied
     */
    PersonalObjectiveCard(PersonalObjectiveCard toCopy){
        this.objective = toCopy.objective;
    }

    /**
     * Method that checks how many points the player has earned with the personal objective
     *
     * @author Federico
     *
     * @param shelf The shelf that has to be checked
     * @return Returns how many points the player has earned with the personal objective
     */
    int checkObjective(Shelf shelf){
        int correspondingTiles = 0;

        for(Coordinate checkingCoord : objective.getPattern().keySet()){
            if(shelf.getTile(checkingCoord) == objective.getPattern().get(checkingCoord)) correspondingTiles++;
        }

        if(correspondingTiles == 0) return 0;
        else if(correspondingTiles == 1) return 1;
        else if(correspondingTiles == 2) return 2;
        else if(correspondingTiles == 3) return 4;
        else if(correspondingTiles == 4) return 6;
        else if(correspondingTiles == 5) return 9;
        else if(correspondingTiles == 6) return 12;
        else throw new IllegalStateException("Something went wrong while checking the personal objective");
    }

    /**
     * Method that returns the pattern of the personal objective
     *
     * @author Federico
     *
     * @return Returns the pattern of the personal objective
     */
    PersonalObjectivePattern getPattern(){
        return objective;
    }

    public String toString(){
        return objective.toString();
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
            put(new Coordinate(1, 4), Tile.TROFEI); //Cyan
            put(new Coordinate(3, 0), Tile.GATTI); //Green
            put(new Coordinate(2, 2), Tile.LIBRI); //White
            put(new Coordinate(0, 0), Tile.GIOCHI); //Yellow
            put(new Coordinate(1, 2), Tile.CORNICI); //Blue
            put(new Coordinate(1, 4), Tile.PIANTE); //Pink
        }}),

        THIRD_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(0, 2), Tile.TROFEI); //Cyan
            put(new Coordinate(0, 5), Tile.GATTI); //Green
            put(new Coordinate(2, 0), Tile.LIBRI); //White
            put(new Coordinate(4, 1), Tile.GIOCHI); //Yellow
            put(new Coordinate(3, 4), Tile.CORNICI); //Blue
            put(new Coordinate(1, 3), Tile.PIANTE); //Pink
        }}),

        FOURTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(3, 0), Tile.TROFEI); //Cyan
            put(new Coordinate(4, 1), Tile.GATTI); //Green
            put(new Coordinate(1, 4), Tile.LIBRI); //White
            put(new Coordinate(0, 3), Tile.GIOCHI); //Yellow
            put(new Coordinate(2, 2), Tile.CORNICI); //Blue
            put(new Coordinate(2, 5), Tile.PIANTE); //Pink
        }}),

        FIFTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(0, 3), Tile.TROFEI); //Cyan
            put(new Coordinate(2, 1), Tile.GATTI); //Green
            put(new Coordinate(1, 1), Tile.LIBRI); //White
            put(new Coordinate(4, 5), Tile.GIOCHI); //Yellow
            put(new Coordinate(2, 3), Tile.CORNICI); //Blue
            put(new Coordinate(3, 2), Tile.PIANTE); //Pink
        }}),

        SIXTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(1, 1), Tile.TROFEI); //Cyan
            put(new Coordinate(2, 3), Tile.GATTI); //Green
            put(new Coordinate(4, 2), Tile.LIBRI); //White
            put(new Coordinate(2, 5), Tile.GIOCHI); //Yellow
            put(new Coordinate(0, 0), Tile.CORNICI); //Blue
            put(new Coordinate(4, 1), Tile.PIANTE); //Pink
        }}),

        SEVENTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(2, 0), Tile.TROFEI); //Cyan
            put(new Coordinate(4, 4), Tile.GATTI); //Green
            put(new Coordinate(3, 3), Tile.LIBRI); //White
            put(new Coordinate(1, 2), Tile.GIOCHI); //Yellow
            put(new Coordinate(2, 5), Tile.CORNICI); //Blue
            put(new Coordinate(0, 5), Tile.PIANTE); //Pink
        }}),

        EIGHTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(3, 1), Tile.TROFEI); //Cyan
            put(new Coordinate(0, 3), Tile.GATTI); //Green
            put(new Coordinate(4, 2), Tile.LIBRI); //White
            put(new Coordinate(2, 3), Tile.GIOCHI); //Yellow
            put(new Coordinate(4, 0), Tile.CORNICI); //Blue
            put(new Coordinate(1, 4), Tile.PIANTE); //Pink
        }}),

        NINTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(2, 3), Tile.TROFEI); //Cyan
            put(new Coordinate(1, 4), Tile.GATTI); //Green
            put(new Coordinate(3, 1), Tile.LIBRI); //White
            put(new Coordinate(3, 0), Tile.GIOCHI); //Yellow
            put(new Coordinate(4, 5), Tile.CORNICI); //Blue
            put(new Coordinate(0, 2), Tile.PIANTE); //Pink
        }}),

        TENTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(5, 5), Tile.TROFEI); //Cyan
            put(new Coordinate(3, 2), Tile.GATTI); //Green
            put(new Coordinate(0, 3), Tile.LIBRI); //White
            put(new Coordinate(1, 4), Tile.GIOCHI); //Yellow
            put(new Coordinate(1, 1), Tile.CORNICI); //Blue
            put(new Coordinate(0, 3), Tile.PIANTE); //Pink
        }}),

        ELEVENTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(3, 2), Tile.TROFEI); //Cyan
            put(new Coordinate(0, 0), Tile.GATTI); //Green
            put(new Coordinate(2, 5), Tile.LIBRI); //White
            put(new Coordinate(4, 1), Tile.GIOCHI); //Yellow
            put(new Coordinate(2, 3), Tile.CORNICI); //Blue
            put(new Coordinate(1, 4), Tile.PIANTE); //Pink
        }}),

        TWELFTH_PATTERN(new HashMap<Coordinate, Tile>() {{
            put(new Coordinate(4, 2), Tile.TROFEI); //Cyan
            put(new Coordinate(1, 2), Tile.GATTI); //Green
            put(new Coordinate(0, 0), Tile.LIBRI); //White
            put(new Coordinate(3, 4), Tile.GIOCHI); //Yellow
            put(new Coordinate(0, 4), Tile.CORNICI); //Blue
            put(new Coordinate(2, 3), Tile.PIANTE); //Pink
        }}),
        ;
        private final HashMap<Coordinate, Tile> pattern;

        PersonalObjectivePattern(HashMap<Coordinate, Tile> pattern) {
            this.pattern = pattern;
        }

        public HashMap<Coordinate, Tile> getPattern() {
            return pattern;
        }

        public String toString(){
            StringBuilder ret = new StringBuilder();

            for(int j=5; j>=0; j--){
                for(int i=0; i<5; i++){
                    Coordinate printingCoord  = null;
                    for(Coordinate checkingCoord : pattern.keySet()){
                        if(checkingCoord.getX() == i && checkingCoord.getY() == j){
                            printingCoord = checkingCoord;
                        }
                    }

                    if(printingCoord != null){
                        ret.append("|").append(pattern.get(printingCoord).getType().charAt(0)).append(pattern.get(printingCoord).getType().charAt(1));
                    } else {
                        ret.append("|  ");
                    }
                }
                ret.append("|\n");
            }

            return ret.toString();
        }
    }
}
