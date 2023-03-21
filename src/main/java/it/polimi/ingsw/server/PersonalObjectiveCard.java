package it.polimi.ingsw.server;

import java.util.Map;

/**
 * Class that defines the Personal Objective Card
 *
 * @author Federico
 */
public class PersonalObjectiveCard{
    private Map<Coordinate, Tile> objective;

    /**
     * Constructor of the personal objective, creates the objective
     *
     * @author Federico
     *
     * @param objective dictionary containing 6 elements, each element represent a tile type and is keyed by its coordinates on the shelf;
     */
    PersonalObjectiveCard(Map<Coordinate, Tile> objective){
        this.objective = new Map<Coordinate, Tile>();
        this.objective.putAll(objective);
    }

    /**
     * Copy constructor of the personal objective
     *
     * @param toCopy PersonalObjective that has to be copied
     */
    PersonalObjectiveCard(PersonalObjectiveCard toCopy){
        this.objective = new Map<Coordinate, Tile>();
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

    }
}
