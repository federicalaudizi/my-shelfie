package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.socket.SocketClientHandler;
import it.polimi.ingsw.server.exceptions.NonExsistentGameException;
import it.polimi.ingsw.server.exceptions.PlayerIdTakenException;
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
        GameSupervisor gameSupervisor = new GameSupervisor();
        GameController gameController = new GameController(4,"ABCDEF", gameSupervisor);
        ClientHandler clientHandler1 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler2 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler3 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler4 = new SocketClientHandler(new Socket(), gameSupervisor);

        gameController.addPlayer("sassa", clientHandler1);
        gameController.addPlayer("chiari", clientHandler2);
        gameController.addPlayer("fede",clientHandler3 );
        gameController.addPlayer("mario", clientHandler4);

        gameController.notifyDisconnection("chiari");
        gameController.notifyDisconnection("fede");

        gameController.notifyConnection("fede");

        HashMap<String, Integer> c = new HashMap<>();

        c.put("sassa", 1);
        c.put("chiari",0);
        c.put("fede", 1);
        c.put("mario", 1);

        assertEquals(c,gameController.getConnectedPlayers());
    }

    public void testAddPlayer() throws ReachedMaxNumberOfPlayers {
        GameSupervisor gameSupervisor = new GameSupervisor();
        GameController gameController = new GameController(2,"ABCDEF", gameSupervisor);
        ClientHandler clientHandler1 = new SocketClientHandler(new Socket(), gameSupervisor);
        ClientHandler clientHandler2 = new SocketClientHandler(new Socket(), gameSupervisor);

        gameController.addPlayer("sassa", clientHandler1);
        gameController.addPlayer("chiari", clientHandler2);

        HashMap<String, Integer> c = new HashMap<>();
        HashMap<String, ClientHandler> p = new HashMap<>();

        c.put("sassa", 1);
        c.put("chiari",1);

        p.put("sassa", clientHandler1);
        p.put("chiari", clientHandler2);

        assertEquals(c,gameController.getConnectedPlayers());
        assertEquals(p,gameController.getPlayerToClientHandlerMap());
    }

    public void testTestRun() throws PlayerIdTakenException, ReachedMaxNumberOfPlayers, NonExsistentGameException {
        try {
            GameSupervisor gameSupervisor = new GameSupervisor();
            GameController gameController = new GameController(4, "ABCDEF", gameSupervisor);
            Thread thread = new Thread(gameController);
            thread.start();

            ClientHandler clientHandler1 = new FakeClientHandler();
            ClientHandler clientHandler2 = new FakeClientHandler();
            ClientHandler clientHandler3 = new FakeClientHandler();
            ClientHandler clientHandler4 = new FakeClientHandler();

            gameController.addPlayer("sassa", clientHandler1);
            gameController.addPlayer("federica", clientHandler2);
            gameController.addPlayer("federico", clientHandler3);
            gameController.addPlayer("mario", clientHandler4);

            thread.join();

        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}