package it.polimi.ingsw.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final GameSupervisor ongoingGames;

    long thisPlayerId;

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

            //Wait for player to decide if he wants to join or create another game
            String choice = in.readLine();
            System.out.println("Client's choice received: " + choice);

            if(choice.equals("1")) {
                //Create a new game
                System.out.println("Client wants to create a new game");
            }else if(choice.equals("2")) {
                //Join an existing game
                System.out.println("Client wants to join an existing game");
            }else if(choice.equals("3")) {
                //Exit
                ongoingGames.removePlayer(thisPlayerId);
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostName());
                return;
            }

            //Close connection
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to initiate the connection with the client.
     *
     * @author Federico
     *
     * @throws IOException
     */
    private void initiateConnection() throws IOException {
        //Initiate connection
        System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

        String newPlayer = in.readLine();

        if(newPlayer.equals("new")){
            // A new player connected
            thisPlayerId = ongoingGames.addPlayer();
            out.println(thisPlayerId);
            System.out.println("Client's id sent: " + thisPlayerId);
        }else if(ongoingGames.playerExists(Long.parseLong(newPlayer))) {
            // An existing player connected
            thisPlayerId = Long.parseLong(newPlayer);
            System.out.println("Client's id received: " + thisPlayerId);
        }else{
            // The player doesn't exist
            System.out.println("Client's id doesn't exist: " + newPlayer);
            out.println("Your player id is not valid!");
            clientSocket.close();
        }
    }

    /**
     * This method is used when a player wants to create a new game.
     *
     * @author Federico
     */
    private void newGame(){

    }

    /**
     * This method is used when a player wants to join an existing game.
     *
     * @author Federico
     */
    private void joinGame(){

    }

    /**
     * This method is used to play a game, it handles the communication with the player on the client side.
     *
     * @author Federico
     */
    private void playGame(){

    }
}
