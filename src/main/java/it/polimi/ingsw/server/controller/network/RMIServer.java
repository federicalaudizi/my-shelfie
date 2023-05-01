package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public class RMIServer extends Server{

    RMIServer(GameSupervisor ongoingGames) {
        super(ongoingGames);
    }

    @Override
    public void run() {

    }
}
