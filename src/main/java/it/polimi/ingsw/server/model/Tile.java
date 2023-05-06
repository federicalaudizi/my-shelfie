package it.polimi.ingsw.server.model;

/**
 * This class describes the tiles in the game.
 * Every game contains 132 cards, divided equally in 6 groups.
 * Other than the standard types defined by the game rules, two types OUTSIDE_GAME_BOARD and EMPTY describe,
 * respectively, a tile on the board that is not in the playing area and an empty tile on the board.
 * Every enum object has a type and a colour, except for OUTSIDE_GAME_BOARD and EMPTY, where the colour is "N/A"
 * (Not Applicable).
 *
 * @author Mario Merlo
 */
public enum Tile {
    CATS("Cats", "Green", "g"),
    BOOKS("Books", "Beige", "y"),
    GAMES("Games", "Orange", "o"),
    FRAMES("Frames", "Blue", "b"),
    TROPHIES("Trophies", "Aqua", "a"),
    PLANTS("Plants", "Magenta", "m"),
    OUTSIDE_GAME_BOARD("Outside Game Board", "N/A", "x"),
    EMPTY("Empty", "N/A", "e");

    private final String type;
    private final String colour;
    private final String symbol;

    /**
     * Tile class constructor.
     * @param type The type of Tile.
     * @param colour The colour of the Tile.
     *
     * @author Mario Merlo
     */
    Tile(final String type, final String colour, final String symbol){
        this.type = type;
        this.colour = colour;
        this.symbol = symbol;
    }

    /**
     * Copy constructor
     * */
    Tile(Tile other){
        this.type = other.type;
        this.colour = other.colour;
        this.symbol = other.symbol;
    }

    /**
     * Returns the type of the Tile it is called upon.
     * @return the type of the Tile.
     *
     * @author Mario Merlo
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the colour of the Tile it is called upon.
     * @return the colour of the Tile.
     *
     * @author Mario Merlo
     */
    public String getColour() {
        return colour;
    }

    /**
     * Returns the symbol of the Tile it is called upon.
     *
     * @return the symbol of the Tile.
     * */
    public String getSymbol() {
        return symbol;
    }
}
