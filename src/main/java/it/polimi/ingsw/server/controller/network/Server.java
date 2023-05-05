package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public abstract class Server implements Runnable{
    GameSupervisor ongoingGames;

    Server(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }
}
