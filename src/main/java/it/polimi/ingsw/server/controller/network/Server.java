package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public abstract class Server implements Runnable{
    protected GameSupervisor ongoingGames;
    protected int port;

    /**
     * Creates a new instance of the server on the specified port
     * @param port the port on which the server will be listening
     * @param ongoingGames the GameSupervisor instance
     * @author Federico
     */
    protected Server(int port, GameSupervisor ongoingGames) {
        this.port = port;
        this.ongoingGames = ongoingGames;
    }
}
