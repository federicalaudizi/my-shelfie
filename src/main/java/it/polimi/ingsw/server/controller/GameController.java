package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.Command;
import it.polimi.ingsw.server.model.Game;

import java.io.PrintStream;
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

        return null;
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
            Command.CommandCode.GAME_OVER;
        }
    }

    @Override
    public void run() {
        while(!isOver){
            //get tiles
            game.chooseTiles(c1,c2,c3); // capire come dire al player dei common objective
            //get column
            game.insertInShelf(column, tiles);
            turnHandler();
        }
    }
}
