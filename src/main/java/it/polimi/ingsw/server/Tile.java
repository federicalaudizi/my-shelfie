package it.polimi.ingsw.server;

public enum Tile {
    CATS("Cats", "Green"),
    BOOKS("Books", "Beige"),
    GAMES("Games", "Orange"),
    FRAMES("Frames", "Blue"),
    TROPHIES("Trophies", "Aqua"),
    PLANTS("Plants", "Magenta"),
    OUTSIDE_GAME_BOARD("Outside Game Board", "N/A"),
    EMPTY("Empty", "N/A");

    private final String type;
    private final String colour;

    Tile(final String type) {
        this.type = type;
        this.colour = colour;
    }

    public String getType() {
        return type;
    }
}
