package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.exceptions.AnswerNotReadyException;

/**
 * This class handles the exchange of messages with the client and runs as a thread.
 * This class represents che client on the server side, being a thread, after its initialization, it will run on command by the game controller it is associated to.
 * The logic behind it is that it first handshakes with the client, handles its login, then deals with the player the creation or joining of a game.
 * After that, it will be at the disposal of the game controller, which will send the commands that the player has to execute.
 *
 * @author Federico
 */
public abstract class ClientHandler implements Runnable{
    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Federico
     */
    public abstract void run();

    /**
     * This method signals the client handler to send the game state to the client
     *
     * @param gameState the game state to be sent packetized as a String
     * @author Federico
     */
    public abstract void sendGameState(String gameState);

    /**
     * This method signals the client handler to send a command to the client
     *
     * @param command the command to be sent
     * @author Federico
     */
    public abstract void sendCommand(Command command);

    /**
     * This method is used to retrieve the answer to a command sent by the client handler
     *
     * @throws AnswerNotReadyException if the answer is not ready yet
     * @return the answer to the command sent by the client handler as a string
     * @author Federico
     */
    public abstract String getAnswer() throws AnswerNotReadyException;
}
