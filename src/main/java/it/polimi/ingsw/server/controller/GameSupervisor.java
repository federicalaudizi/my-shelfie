package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.FakeClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the creation of games and the association of players to games.
 * It also keeps track of the games that are currently running, the players that are currently playing and the players that are currently waiting for a game to start.
 *
 * @author Federico
 */
public class GameSupervisor implements Runnable{
    private final ConcurrentHashMap<String, GameController> games; //Associates a game to its id
    private final ConcurrentHashMap<String, ClientHandler> players; //Associates a player to his client handler, client handler is null if the player is not connected
    private final ConcurrentHashMap<String, String> playersGames; //Associates a player to the game he is playing

    public GameSupervisor(){
        games = new ConcurrentHashMap<>();
        players = new ConcurrentHashMap<>();
        playersGames = new ConcurrentHashMap<>();
    }

    public GameSupervisor(JSONObject other){
        this.games = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.playersGames = new ConcurrentHashMap<>();

        JSONArray games = other.getJSONArray("games");
        for(int i = 0; i < games.length(); i++){
            JSONObject game = games.getJSONObject(i);
            this.games.put(game.getString("gameId"), new GameController(game.getJSONObject("game"), this));
        }

        JSONArray players = other.getJSONArray("players");
        for(int i = 0; i < players.length(); i++){
            this.players.put(players.getString(i), new FakeClientHandler());
        }

        JSONArray playersGames = other.getJSONArray("playersGames");
        for(int i = 0; i < playersGames.length(); i++){
            JSONObject playerGame = playersGames.getJSONObject(i);
            this.playersGames.put(playerGame.getString("playerId"), playerGame.getString("gameId"));
        }

        for(GameController game : this.games.values()){
            new Thread(game).start();
        }
    }

    @Override
    public void run() {
        Path path = Paths.get("gamesaves/");
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                synchronized (this) {
                    this.wait(5*60000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // TODO: check if this is the right way to do it
            for(String player : playersGames.keySet()){
                if(playersGames.get(player).equals("TERMINATED")){
                    playersGames.remove(player);
                    players.remove(player);
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = "gamesaves/"+timeStamp + ".txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                System.out.println("Supervisor: saving state on "+filePath);
                writer.write(this.toJson().toString(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method adds a new player to the list of players that are logged in
     *
     * @param handler the client handler of the player
     * @author Federico
     */
    public void newUser(String playerId, ClientHandler handler) throws PlayerIdTakenException {
        // If a player is in game, we don't want him to be able to login as a new user
        if(players.containsKey(playerId)) throw new PlayerIdTakenException();
        players.put(playerId, handler);
    }

    /**
     * Allows for a user that previously logged in to be recognized again
     *
     * @param playerId the id of the player
     * @param handler  the client handler of the player
     * @author Federico
     */
    public void oldUser(String playerId, ClientHandler handler) throws PlayerDoesNotExistsException {
        if(!players.containsKey(playerId) || !playersGames.containsKey(playerId)) throw new PlayerDoesNotExistsException();
        players.put(playerId, handler);
    }

    /**
     * This method removes a player from the list of players that are logged in
     *
     * @param playerId the id of the player that is being removed
     */
    public void removeUser(String playerId){
        players.remove(playerId);
    }

    /**
     * This method creates a new game and adds it to the list of games, after its creation, joinGame should be called
     *
     * @param numberOfPlayers the number of players that will play the game
     * @return the id of the game
     * @author Federico
     */
    public String newGame(int numberOfPlayers) {
        String newGameId = randomString();
        GameController game = new GameController(numberOfPlayers, newGameId, this);
        games.put(newGameId, game);

        new Thread(game).start();

        return newGameId;
    }

    // This method is for testing only
    @Deprecated
    public String newGameTest(int numberOfPlayers) {
        String newGameId = randomString();
        GameController game = new GameController(numberOfPlayers, newGameId, this);
        games.put(newGameId, game);

        return newGameId;
    }

    /**
     * This method adds a player to a game
     *
     * @param playerId the id of the player
     * @param gameId   the id of the game
     * @author Federico
     */
    public void joinGame(String playerId, String gameId) throws NonExistentGameException, ReachedMaxNumberOfPlayers {
        if(!games.containsKey(gameId)) throw new NonExistentGameException();
        GameController game = games.get(gameId);
        game.addPlayer(playerId, players.get(playerId));
        playersGames.put(playerId, gameId);
    }

    /**
     * This method registers that a player is not connected to the server anymore
     *
     * @param playerId the username of the just disconnected player
     * @author Federico
     */
    public void notifyDisconnection(String playerId){
        System.out.println("Supervisor: "+playerId+" disconnected");
        String gameId = playersGames.get(playerId);
        if(gameId != null) {
            GameController game = games.get(gameId);
            if (game != null) game.notifyDisconnection(playerId);
        }
    }

    /**
     * This method registers that a player is connected to the server
     *
     * @param playerId the username of the just connected player
     */
    public void notifyConnection(String playerId){
        System.out.println("Supervisor: "+playerId+" connected");
        String gameId = playersGames.get(playerId);
        if(gameId != null) {
            GameController game = games.get(gameId);
            if (game != null) game.notifyConnection(playerId);
        }
    }

    /**
     * This method returns the list of the ids of the games that are currently running
     *
     * @return the list of the ids of the games that are currently running
     * @author Federico
     */
    public ArrayList<String> getGameIds() throws NoGamesException{
        if(games.isEmpty()) throw new NoGamesException();
        return new ArrayList<>(games.keySet());
    }

    /**
     * This method returns whether a player exists or not
     *
     * @param playerId the id of the player
     * @return true if the player exists, false otherwise
     * @author Federico
     */
    public boolean userExists(String playerId) {
        return players.containsKey(playerId);
    }

    /**
     * This method returns whether a game exists or not
     *
     * @param gameId the id of the game
     * @return true if the game exists, false otherwise
     * @author Federico
     */
    public boolean gameExists(String gameId) {
        return games.containsKey(gameId);
    }

    /**
     * This method returns whether a player is in a game or not
     *
     * @param playerId the id of the player
     * @return true if the player is in a game, false otherwise
     * @author Federico
     */
    public boolean userIsInGame(String playerId) {
        return playersGames.containsKey(playerId);
    }

    /**
     * This method ends a game, the game controller should call this method right before it stops its thread.
     * It deletes the record of the game and sets the association of the players to the game to "TERMINATED" so that it can be later removed at next startup
     *
     * @param gameId the id of the game
     * @author Federico
     */
    public void gameOver(String gameId) {
        //Remove players association to the game
        for(String playerId : players.keySet()){
            if(playersGames.get(playerId).equals(gameId)) {
                //Remove player from game
                //TODO: test if this is the right way to do it
                playersGames.remove(playerId);
                playersGames.put(playerId, "TERMINATED");
            }
        }
        //Remove record of the game
        games.remove(gameId);
    }

    /**
     * This method returns the client handler of a player by its id
     *
     * @param playerId the id of the player
     * @return the client handler of the player
     * @author Federico
     */
    public ClientHandler getClientHandlerById(String playerId){
        return players.get(playerId);
    }

    /**
     * This method returns the game controller of a game by its id
     *
     * @param gameId the id of the game
     * @return the game controller of the game
     * @author Federico
     */
    public GameController getGameControllerById(String gameId){
        return games.get(gameId);
    }

    /**
     * This method returns whether two GameSupervisors contain the same players, games and player to game associations
     *
     * @param other the other GameSupervisor
     * @return true if the two GameSupervisors are equal, false otherwise
     * @author Federico
     */
    public boolean equals(GameSupervisor other){
        boolean games = true;
        boolean players = true;
        boolean playersGames = true;

        for(String gameId : this.games.keySet()){
            if (!other.games.containsKey(gameId)) {
                // Check if all game Ids are the same
                games = false;
                break;
            }
        }

        for(String playerId : this.players.keySet()){
            if (!other.players.containsKey(playerId)) {
                // Check if all player Ids are the same
                players = false;
                break;
            }
        }

        for(String playerId : this.playersGames.keySet()){
            if (!other.playersGames.containsKey(playerId)) {
                // Check if all player Ids are the same
                playersGames = false;
                break;
            } else if (!this.playersGames.get(playerId).equals(other.playersGames.get(playerId))) {
                // Check if all player to game associations are the same
                playersGames = false;
                break;
            }
        }

        return games && players && playersGames;
    }

    /**
     * This method returns a JSONObject representation of the GameSupervisor containing all players, all the games that are
     * actually running (all players joined the game). For example:
     * {
     *     "games":[{"gameId":"game123", "game":{game.toJson()}},...],
     *     "players":["player1","player2"...],
     *     "playersGames":[{"playerId": "player1", "gameId": "code123"}, ...]
     * }
     *
     * @return a JSONObject representation of the GameSupervisor
     */
    public JSONObject toJson(){
        JSONObject json = new JSONObject();

        //JSONArray like this: [{"gameId": "gameId", "game": {game}}, ...]
        JSONArray gamesArray = new JSONArray();
        for(String gameId : games.keySet()){
            JSONObject game = new JSONObject();
            game.put("gameId", gameId);
            try {
                game.put("game", games.get(gameId).toJson());
            } catch (NonExistentGameException e) {
                continue;
            }
            gamesArray.put(game);
        }
        json.put("games", gamesArray);

        json.put("players", players.keySet());

        //JSONArray like this: [{"playerId": "player1", "gameId": "code123"}, ...]
        JSONArray playersGamesArray = new JSONArray();
        for(String playerId : playersGames.keySet()){
            JSONObject playerGame = new JSONObject();
            String gameId = playersGames.get(playerId);
            if(!games.get(gameId).isStarted()) continue;
            playerGame.put("playerId", playerId);
            playerGame.put("gameId", gameId);
            playersGamesArray.put(playerGame);
        }
        json.put("playersGames", playersGamesArray);

        return json;
    }

    /**
     * This is a helper method that generates random strings
     *
     * @return a random string
     * @author Federico
     */
    private String randomString(){
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = (int)(characters.length() * Math.random());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
