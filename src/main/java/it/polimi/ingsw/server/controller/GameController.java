package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Game Controller
 * @author Federica & Sara
 * */

public class GameController<T> implements Runnable {

    private final Object lock = new Object();

    private final Game game;
    private final Map<String, ClientHandler> playerToClientHandlerMap;
    private  Map<String, Integer> connectedPlayers;
    private final List<String> turnOrder;
    private final T gameId;
    private boolean isOver;
    private int currentTurnIndex;



      GameController(int playerNumber, T gameId) {
          playerToClientHandlerMap = new HashMap<>();
          this.gameId = gameId;
          game = new Game(playerNumber);
          isOver= false;
          turnOrder = new ArrayList<>();
          currentTurnIndex = game.getCurrentPlayerIndex();
    }


    public void notifyDisconnection(String playerId){
        connectedPlayers.put(playerId, 0);
    }

    public void notifyConnection(String playerId){
        connectedPlayers.put(playerId, 1);
    }
    /**
     * sends the client the game state*/
    public void getGameState() throws IOException {
          String currentPlayerId = turnOrder.get(currentTurnIndex);
          getClientHandler(currentPlayerId).sendGameState(game.toJson());
    }

    /***/
    void addPlayer(String playerId, ClientHandler handler) throws ReachedMaxNumberOfPlayers {
        if (playerToClientHandlerMap.size() >= game.getNumberOfPlayers()){
            throw new ReachedMaxNumberOfPlayers();
        }
        connectedPlayers.put(playerId, 1);
        playerToClientHandlerMap.put(playerId, handler);
    }

    /**
     * @param playerId of the client handler
     * @return Client Handler referred to that playerId*/
    ClientHandler getClientHandler(String playerId){
          return playerToClientHandlerMap.get(playerId);
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
    }


    @Override
    public void run() {
        while(!isOver){
            int column;
            Tile[] tiles;
            Coordinate[] coordinates;
            String currentPlayerId = turnOrder.get(currentTurnIndex);

            if (connectedPlayers.get(currentPlayerId) == 0 && connectedPlayers.values().stream().filter(value -> value == 1).count() > 1){
                turnHandler();
                continue;
            } else if (connectedPlayers.values().stream().filter(value -> value == 1).count() == 1){
                try {
                    // Wait for the condition to be verified, with a timeout of 15 seconds
                    synchronized (lock) {
                        long startTime = System.currentTimeMillis();
                        long elapsedTime = 0;
                        while (connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1 && elapsedTime < 15000) {
                            lock.wait(5000 - elapsedTime);
                            elapsedTime = System.currentTimeMillis() - startTime;
                        }
                        if (connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1){
                            getClientHandler(currentPlayerId).gameOver((JSONObject) game.getRankedPlayers());
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
                int numCollObj = game.insertInShelf(column, tiles);
                if (numCollObj == 0){
                    getClientHandler(currentPlayerId).sendGameState(game.toJson());
                }else{
                    getClientHandler(currentPlayerId).sendGameState(game.toJson(), numCollObj);
                }
                getClientHandler(currentPlayerId).sendOk();

            } catch (tooManyTilesException | notEnoughTilesException | fullColumnException e) {
                getClientHandler(currentPlayerId).badColumn();
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            turnHandler();
        }
    }
}
