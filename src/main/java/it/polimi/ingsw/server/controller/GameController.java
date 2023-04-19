package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.Command;
import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;

import java.util.ArrayList;



/**
 * Game Controller
 * @author Federica & Sara
 * */

public class GameController<T> implements Runnable {

    private final Game game;
    private final ArrayList<T> players;
    private final T gameId;
    private boolean isOver;

      GameController(int playerNumber, T gameId ) {
          players= new ArrayList<T>();
          this.gameId = gameId;
          game = new Game(playerNumber);
          isOver= false;
    }

    T getGameState(){
        return new Command(Command.CommandCode.GAME_UPDATE, "Game status");
    }

    void addPlayer(T handler){
        players.add(handler);
    }

    void setPlayerConnectionStatus(boolean status, T player){

    }


    /**
     * This method decides who's next turn modifying the currentPlayerIndex.
     * If it is the last turn the game goes on until it reaches the last player
     */
    void turnHandler(){
        if(game.isLastTurn()&& game.getCurrentPlayerIndex()!=game.getLastPlayer()){
            game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % players.size());
        } else if (!game.isLastTurn()) {
            game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % players.size());
        }
        else{
            isOver = true;
            new Command (Command.CommandCode.GAME_OVER, "The game is over");
        }
    }

    //capire chi gestisce ingresso primo giocatore e chiede numero di giocatori
    public void setUpGame(){
        int playerNumber;
        playerNumber = new Command(Command.CommandCode.NEW_GAME_RESPONSE, "set player number");
        for(int i=0; i<playerNumber;i++){
            players.add()
        }
    }

    @Override
    public void run() {
        while(!isOver){
            int column;
            Tile[] tiles;
            Coordinate[] coordinates;
            coordinates = new Command(Command.CommandCode.TILES_RESPONSE, "you've chosen your tiles");
            try {
                tiles = game.chooseTiles(coordinates[0],coordinates[1],coordinates[2]);
                new Command(Command.CommandCode.OK, "Ok");
            } catch (TileUnpickableException e) {
                throw new RuntimeException(e);
                new Command(Command.CommandCode.BAD_TILES_ERROR, "Error Tiles");
            }
            column = new Command(Command.CommandCode.COLUMN_RESPONSE, "Column");
            try {
                int k = game.insertInShelf(column, tiles);
                if(k==1){
                    new Command(Command.CommandCode.COLLECTIVE_OBJ_ACHIEVED, "You achieved the first collective obj");
                } else if (k==2) {
                    new Command(Command.CommandCode.COLLECTIVE_OBJ_ACHIEVED, "You achieved the second collective obj");
                }
            } catch (tooManyTilesException | notEnoughTilesException | fullColumnException e) {
                throw new RuntimeException(e);
                new Command(Command.CommandCode.BAD_COLUMN_ERROR, "Column error");
            }
            turnHandler();
        }
    }
}
