package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Game Controller
 * @author Federica & Sara
 * */

public class GameController<T> implements Runnable {

    private final Game game;
    private final Map<String, ClientHandler> playerToClientHandlerMap;
    private final List<String> turnOrder;
    private final T gameId;
    private boolean isOver;
    private int currentTurnIndex;
    private int maxPlayers;

      GameController(int playerNumber, T gameId) {
          playerToClientHandlerMap = new HashMap<>();
          //aggiungere effettivamente giocatori e eccezione per raggiungimento giocatori
          // quando si connette il primo addUser, connessione  e disocnnessione
          //gestire salto giocatori
          this.gameId = gameId;
          game = new Game(playerNumber);
          isOver= false;
          turnOrder = new ArrayList<>();
          currentTurnIndex = game.getCurrentPlayerIndex();
          maxPlayers = playerNumber;
    }


    /**
     * sends the client the game state*/
    void getGameState(){
          String currentPlayerId = turnOrder.get(currentTurnIndex);
          getClientHandler(currentPlayerId).sendGameState();
    }

    /***/
    void addPlayer(String playerId, ClientHandler handler) throws MaxPlayersReachedException {
        if(playerToClientHandlerMap.size() >= maxPlayers)
            throw new MaxPlayersReachedException();
        playerToClientHandlerMap.put(playerId, handler);
    }

    /**
     * @param playerId of the client handler
     * @return Client Handler referred to that playerId*/
    ClientHandler getClientHandler(String playerId){
          return playerToClientHandlerMap.get(playerId);
    }


    void setPlayerConnectionStatus(boolean status, T player){

    }


    /**
     * This method decides who's next turn modifying the currentTurnIndex.
     * If it is the last turn the game goes on until it reaches the last player
     */
    void turnHandler(){
        if(currentTurnIndex % (turnOrder.size()-1) != 0 && !game.isLastTurn()){
            currentTurnIndex = (currentTurnIndex +1 )% turnOrder.size();
        }
        else{
            isOver = true;
            String currentPlayerId = turnOrder.get(currentTurnIndex);
            getClientHandler(currentPlayerId).gameOver((JSONObject) game.getRankedPlayers());
        }
        game.nextTurn();
    }


    @Override
    public void run() {
        while(!isOver){
            int column;
            Tile[] tiles;
            Coordinate[] coordinates;
            String currentPlayerId = turnOrder.get(currentTurnIndex);
            coordinates = getClientHandler(currentPlayerId).getTiles();
            try {
                tiles = game.chooseTiles(coordinates[0],coordinates[1],coordinates[2]);
                getClientHandler(currentPlayerId).sendOk();
            } catch (TileUnpickableException e) {
                getClientHandler(currentPlayerId).badTile();
                throw new RuntimeException(e);
            }
            column = getClientHandler(currentPlayerId).getColumn();
            getClientHandler(currentPlayerId).sendOk();
            try {
                game.insertInShelf(column, tiles); //vogliamo avvisare quando qualcuno raggiunge obiettivo comune?
                getClientHandler(currentPlayerId).sendOk();

            } catch (tooManyTilesException | notEnoughTilesException | fullColumnException e) {
                getClientHandler(currentPlayerId).badColumn();
                throw new RuntimeException(e);
            }
            turnHandler();
        }
    }
}
