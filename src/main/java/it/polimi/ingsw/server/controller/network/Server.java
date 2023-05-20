package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public abstract class Server implements Runnable{
    protected GameSupervisor ongoingGames;
    protected int port;

    protected Server(int port, GameSupervisor ongoingGames) {
        this.port = port;
        this.ongoingGames = ongoingGames;
    }
}
