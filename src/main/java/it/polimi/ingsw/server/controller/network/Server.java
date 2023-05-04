package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;

public abstract class Server implements Runnable{
    GameSupervisor ongoingGames;

    Server(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    public static void main(String[] args) {
        GameSupervisor games = new GameSupervisor();

        Server socketServer = new SocketServer(5000, games);
        //Server rmiServer = new RMIServer(games);

        socketServer.run();
        //rmiServer.run();
    }
}
