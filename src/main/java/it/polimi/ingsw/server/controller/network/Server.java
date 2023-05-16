package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public abstract class Server implements Runnable{
    protected GameSupervisor ongoingGames;

    protected Server(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }
}
