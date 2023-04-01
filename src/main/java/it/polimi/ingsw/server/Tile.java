package it.polimi.ingsw.server;

public enum Tile {
    GATTI("Gatti"),
    LIBRI("Libri"),
    GIOCHI("Giochi"),
    CORNICI("Cornici"),
    TROFEI("Trofei"),
    PIANTE("Piante"),
    EMPTY("Empty"),
    OUTSIDE_GAME_BOARD("Outside Game Board");

    private final String type;

    Tile(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
