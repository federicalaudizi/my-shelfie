package it.polimi.ingsw.server.controller;

<<<<<<< HEAD
public class GameController {
=======
import it.polimi.ingsw.server.Coordinate;
import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.Player;
import it.polimi.ingsw.server.exceptions.TileUnpickableException;

public class GameController {
    private Game game;

    /**
     * setta numero giocstori inizio partita
     * */
    public void setNumberOfPlayers(int numOfPlayers){
        this.game = new Game(numOfPlayers);
    }

    /**
     * prende cordinate scelte dal giocatore*/
    public void takeCoordinates(Player player){
        Coordinate c1 = view.promptCoordinateSelection();  // riceve coordinate dalla view
        Coordinate c2 = view.promptCoordinateSelection();
        Coordinate c3 = view.promptCoordinateSelection();

        try{
            game.playerTurn(c1,c2,c3);
        } catch (TileUnpickableException e) {
            throw new RuntimeException(e);
            view.displayErrorMessage(); // manda sulla view messaggio di errore
        }

        int colonna = view.promptColumn();
        add
        //manda a view nuovo stato
    }
>>>>>>> e8b4362 (Draft of controller)
}
