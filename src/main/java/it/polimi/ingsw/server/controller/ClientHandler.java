package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.exceptions.ConnectionEndedException;
import it.polimi.ingsw.server.exceptions.NonExsistentGameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class handles the exchange of messages with the client and runs as a thread
 */
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final GameSupervisor ongoingGames;
    private long thisPlayerId;

    // The game controller is the object through which the client plays the game, every move should be synchronized on this object
    // so that only one player at time can interact with the game
    private GameController currentGame;
    private long currentGameId;

    public ClientHandler(Socket clientSocket, GameSupervisor ongoingGames){
        this.clientSocket = clientSocket;
        this.ongoingGames = ongoingGames;

        try {
            // Get the input and output streams of the socket
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            initiateConnection();
            mainMenu();
        } catch (ConnectionEndedException e) {
            System.out.println(e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * This method is used to initiate the connection with the client. It recieves from the client its past player's id or the request for a new one.
     *
     * @author Federico
     *
     * @throws ConnectionEndedException if an error occours while reading the command or the client sends an invalid id
     */
    private void initiateConnection() throws ConnectionEndedException{
        // Initiate connection
        System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

        // Recieves form the player its id or request for a new one
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new ConnectionEndedException("An error occurred while reading the command from the client");
        }

        if(command.equals("new")){
            // A new player connected
            thisPlayerId = ongoingGames.addPlayer();
            // Sends the new player id
            out.println(thisPlayerId);
            System.out.println("Client's id sent: " + thisPlayerId);
        }else if(ongoingGames.playerExists(Long.parseLong(command))) {
            // An existing player connected
            thisPlayerId = Long.parseLong(command);
            System.out.println("Client's id received: " + thisPlayerId);
        }else{
            // The player doesn't exist
            System.out.println("Client's id doesn't exist: " + command);
            out.println("Your player id is not valid!");
            throw new ConnectionEndedException("The player had an invalid id");
        }
    }

    /**
     * Handles the client's main menu
     *
     * @author Federico
     *
     * @throws ConnectionEndedException if the client decides to terminate the connection
     */
    private void mainMenu() throws ConnectionEndedException {
        //Wait for player to decide if he wants to join or create another game
        String choice = null;
        try {
            choice = in.readLine();
        } catch (IOException e) {
            throw new ConnectionEndedException("An error occured while reading the client's command!");
        }
        System.out.println("Client's choice received: " + choice);

        switch (choice) {
            case "1" ->
                //Create a new game
                    System.out.println("Client wants to create a new game");
            case "2" ->
                //Join an existing game
                    System.out.println("Client wants to join an existing game");
            case "3" -> {
                //Exit
                ongoingGames.removePlayer(thisPlayerId);
                System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostName());
                throw new ConnectionEndedException("The client wanted to disconnect!");
            }
        }
    }

    /**
     * This method is used when a player wants to create a new game.
     *
     * @author Federico
     */
    private void newGame(){
        currentGameId = ongoingGames.newGame();
        try {
            currentGame = ongoingGames.joinGame(thisPlayerId, currentGameId);
        } catch (NonExsistentGameException ignored) {}

        //TODO: The player should now create the actual game inside the controller and do all the other stuff

        playGame();
    }

    /**
     * This method is used when a player wants to join an existing game.
     *
     * @author Federico
     */
    private void joinGame(){
        //TODO: Here the handler should send the player the list of available games or just wait for the game's id
        try {
            currentGameId = Long.parseLong(in.readLine());
        } catch (IOException e) {
            //TODO: Warn the client that an error occoured and it has to start all over again;
            joinGame();
            return;
        }
        try {
            currentGame = ongoingGames.joinGame(thisPlayerId, currentGameId);
        } catch (NonExsistentGameException e) {
            //TODO: warn the client that the game was not found and that has to try again
            joinGame();
            return;
        }
        playGame();
    }

    /**
     * This method is used to play a game, it handles the communication with the player on the client side.
     *
     * @author Federico
     */
    private void playGame(){

    }

    /**
     * This method sens the client the whole state if the game so that it can display it to the player
     */
    private void sendGameState(){

    }
}
