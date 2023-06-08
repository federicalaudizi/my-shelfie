package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.FakeClientHandler;
import it.polimi.ingsw.server.exceptions.NonExistentGameException;
import it.polimi.ingsw.server.exceptions.PlayerIdTakenException;
import it.polimi.ingsw.server.exceptions.ReachedMaxNumberOfPlayers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameSupervisor gameSupervisor;
    GameController gameController1;
    GameController gameController2;
    FakeClientHandler player1;
    FakeClientHandler player2;
    String gameId;

    @BeforeEach
    void setUp() {
        gameSupervisor = new GameSupervisor();
    }

    @Test
    void notifyDisconnection() throws ReachedMaxNumberOfPlayers {
        gameController1 = new GameController(2, "gameId", gameSupervisor);
        FakeClientHandler player1 = new FakeClientHandler();
        FakeClientHandler player2 = new FakeClientHandler();
        gameController1.addPlayer("pippo", player1);
        gameController1.addPlayer("pluto", player2);
        assertSame(player1, gameController1.getPlayerToClientHandlerMap().get("pippo"));
        assertSame(player2, gameController1.getPlayerToClientHandlerMap().get("pluto"));
        gameController1.notifyDisconnection("pippo");
        assertEquals(0, (int) gameController1.getConnectedPlayers().get("pippo"));
    }

    @Test
    void notifyConnection() throws ReachedMaxNumberOfPlayers {
        gameController1 = new GameController(2, "gameId", gameSupervisor);
        FakeClientHandler player1 = new FakeClientHandler();
        FakeClientHandler player2 = new FakeClientHandler();
        gameController1.addPlayer("pippo", player1);
        gameController1.addPlayer("pluto", player2);
        gameController1.notifyDisconnection("pippo");
        gameController1.notifyConnection("pippo");
        assertEquals(1, (int) gameController1.getConnectedPlayers().get("pippo"));
    }

    @Test
    void addPlayer() throws ReachedMaxNumberOfPlayers {
        gameController1 = new GameController(2, "gameId", gameSupervisor);
        FakeClientHandler player1 = new FakeClientHandler();
        FakeClientHandler player2 = new FakeClientHandler();
        gameController1.addPlayer("pippo", player1);
        gameController1.addPlayer("pluto", player2);
        assertTrue(player1 == gameController1.getPlayerToClientHandlerMap().get("pippo"));
        assertTrue(player2 == gameController1.getPlayerToClientHandlerMap().get("pluto"));
        assertThrows(ReachedMaxNumberOfPlayers.class, () -> gameController1.addPlayer("paperino", new FakeClientHandler()));
    }

    @Test
    void toJson() throws ReachedMaxNumberOfPlayers, NonExistentGameException, PlayerIdTakenException, InterruptedException {
        gameId = gameSupervisor.newGameTest(2);
        gameController1 = gameSupervisor.getGameControllerById(gameId);

        assertThrows(NonExistentGameException.class, () -> gameController1.toJson());
        gameSupervisor.newUser("player1", new FakeClientHandler());
        gameSupervisor.joinGame("player1", gameId);
        assertThrows(NonExistentGameException.class, () -> gameController1.toJson());
        gameSupervisor.newUser("player2", new FakeClientHandler());
        gameSupervisor.joinGame("player2", gameId);

        Thread t = new Thread(gameController1);
        t.start();
        t.join();

        gameController2 = new GameController(gameController1.toJson(), gameSupervisor);

        System.out.println(gameController1.toJson());
        System.out.println(gameController2.toJson());

        assertEquals(gameController1.toJson().toString(), gameController2.toJson().toString());

        assertTrue(gameController1.equals(gameController2));
    }
}