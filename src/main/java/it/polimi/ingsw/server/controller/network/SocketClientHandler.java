package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.GameSupervisorString;
import it.polimi.ingsw.server.exceptions.AnswerNotReadyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static it.polimi.ingsw.server.controller.network.Command.CommandCode.*;

/**
 * This class handles the exchange of messages with the client and runs as a thread.
 * This class represents che client on the server side, being a thread, after its initialization, it will run on command by the game controller it is associated to.
 * The logic behind it is that it first handshakes with the client, handles its login, then deals with the player the creation or joining of a game.
 * After that, it will be at the disposal of the game controller, which will send the commands that the player has to execute.
 */
public class SocketClientHandler extends ClientHandler{
    private final Socket clientSocket;
    private PrintWriter dataOut;
    private BufferedReader dataIn;
    private final GameSupervisorString ongoingGames;
    private GameController<String> game; //Is this the right way to do it?
    private long thisPlayerId;

    private boolean sendGameStateFlag = false;
    private boolean sendCommandFlag = false;
    private boolean answerReadyFlag = false;
    private Command command;
    private String gameState;
    private String answer;

    public SocketClientHandler(Socket clientSocket, GameSupervisorString ongoingGames) {
        this.clientSocket = clientSocket;
        this.ongoingGames = ongoingGames;

        try {
            // Get the input and output streams of the socket
            dataOut = new PrintWriter(clientSocket.getOutputStream(), true);
            dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //TODO: Implement handshake
        dataOut.println(new Command(WELCOME, "Welcome to the game!"));
        //TODO: Implement login

        //TODO: Implement game creation/joining

        //TODO: Implement communication logic
        while(true){
            if(sendGameStateFlag){
                dataOut.println(gameState);
                sendGameStateFlag = false;
            }

            if(sendCommandFlag){
                dataOut.println(command);
                sendCommandFlag = false;
                //TODO: Implement answer logic
                answerReadyFlag = true;
            }

            try {
                game.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendGameState(String gameState) {
        this.gameState = gameState;
        sendGameStateFlag = true;
    }

    @Override
    public void sendCommand(Command command) {
        this.command = command;
        sendCommandFlag = true;
    }

    @Override
    public String getAnswer() throws AnswerNotReadyException {
        if(answerReadyFlag){
            answerReadyFlag = false;
            return answer;
        } else {
            throw new AnswerNotReadyException("Answer not ready yet");
        }
    }
}
