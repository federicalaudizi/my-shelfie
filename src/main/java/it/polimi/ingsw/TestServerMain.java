package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Server;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;

public class TestServerMain {
    public static void main(String[] args) {
        GameSupervisor games = new GameSupervisor();

        Server socketServer = new SocketServer(8000, games);

        socketServer.run();
    }
}
