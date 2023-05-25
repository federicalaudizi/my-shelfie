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
    void notifyDisconnection() {
    }

    @Test
    void notifyConnection() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void toJson() throws ReachedMaxNumberOfPlayers, NonExistentGameException, PlayerIdTakenException, InterruptedException {
        gameId = gameSupervisor.newGame(2);
        gameController1 = gameSupervisor.getGameControllerById(gameId);

        assertThrows(NonExistentGameException.class, () -> gameController1.toJson());
        gameSupervisor.newUser("player1", new FakeClientHandler());
        gameSupervisor.joinGame("player1", gameId);
        assertThrows(NonExistentGameException.class, () -> gameController1.toJson());
        gameSupervisor.newUser("player2", new FakeClientHandler());
        gameSupervisor.joinGame("player2", gameId);

        //Wait a bit for the game to start
        Thread.sleep(1000);

        gameController2 = new GameController(gameController1.toJson(), gameSupervisor);

        System.out.println(gameController1.toJson());
        System.out.println(gameController2.toJson());

        assertEquals(gameController1.toJson().toString(), gameController2.toJson().toString());

        assertTrue(gameController1.equals(gameController2));
    }

    @Test
    void getPlayerToClientHandlerMap() {
    }

    @Test
    void getConnectedPlayers() {
    }
}