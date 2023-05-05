package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.SocketClientHandler;
import it.polimi.ingsw.server.exceptions.ReachedMaxNumberOfPlayers;
import junit.framework.TestCase;

import java.net.Socket;
import java.util.HashMap;

public class GameControllerTest extends TestCase {

    public void testNotifyDisconnection() throws ReachedMaxNumberOfPlayers {
        GameSupervisor gameSupervisor = new GameSupervisor();
        GameController gameController = new GameController(3,"ABCDEF", gameSupervisor);
        ClientHandler clientHandler1 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler2 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler3 = new SocketClientHandler(new Socket(), gameSupervisor);

        gameController.addPlayer("sassa", clientHandler1);
        gameController.addPlayer("chiari", clientHandler2);
        gameController.addPlayer("fede",clientHandler3 );

        gameController.notifyDisconnection("chiari");
        gameController.notifyDisconnection("fede");

        HashMap<String, Integer> c = new HashMap<>();

        c.put("sassa", 1);
        c.put("chiari",0);
        c.put("fede", 0);

        assertEquals(c,gameController.getConnectedPlayers());

    }

    public void testNotifyConnection() throws ReachedMaxNumberOfPlayers {

    }

    public void testAddPlayer() throws ReachedMaxNumberOfPlayers {

    }

    public void testTestRun() {
    }
}