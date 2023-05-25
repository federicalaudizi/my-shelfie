package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.FakeClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSupervisorTest {
    GameSupervisor gameSupervisor;
    ClientHandler clientHandler;
    ClientHandler clientHandler2;
    String gameId;

    @BeforeEach
    void setUp() {
        gameSupervisor = new GameSupervisor();
        clientHandler = new FakeClientHandler();
        clientHandler2 = new FakeClientHandler();
    }

    @Test
    void run() throws InterruptedException {
        Thread supervisorThread = new Thread(gameSupervisor);
        supervisorThread.start();
    }

    @Test
    void newUser() throws PlayerIdTakenException {
        gameSupervisor.newUser("testId", clientHandler);
        assertThrows(PlayerIdTakenException.class, () -> gameSupervisor.newUser("testId", clientHandler));
        assertTrue(gameSupervisor.userExists("testId"));
        assertFalse(gameSupervisor.userExists("testId2"));
        assertFalse(gameSupervisor.userIsInGame("testId"));
        assertEquals(clientHandler, gameSupervisor.getClientHandlerById("testId"));
    }

    @Test
    void oldUser() throws PlayerIdTakenException, PlayerDoesNotExistsException {
        assertThrows(PlayerDoesNotExistsException.class, () -> gameSupervisor.oldUser("testId", new FakeClientHandler()));
        ClientHandler clientHandler2 = new FakeClientHandler();
        gameSupervisor.newUser("testId", clientHandler);
        gameSupervisor.oldUser("testId", clientHandler2);
        assertEquals(clientHandler2, gameSupervisor.getClientHandlerById("testId"));
        assertNotEquals(clientHandler, gameSupervisor.getClientHandlerById("testId"));
    }

    @Test
    void removeUser() throws PlayerIdTakenException {
        gameSupervisor.newUser("testId", new FakeClientHandler());
        assertTrue(gameSupervisor.userExists("testId"));
        gameSupervisor.removeUser("testId");
        assertFalse(gameSupervisor.userExists("testId"));
        assertFalse(gameSupervisor.userIsInGame("testId"));
    }

    @Test
    void newGame() throws NoGamesException {
        assertThrows(NoGamesException.class, () -> gameSupervisor.getGameIds());
        gameId = gameSupervisor.newGame(2);
        assertTrue(gameSupervisor.gameExists(gameId));
        assertTrue(gameSupervisor.getGameIds().contains(gameId));
    }

    @Test
    void joinGame() throws PlayerIdTakenException, ReachedMaxNumberOfPlayers, NonExistentGameException {
        gameSupervisor.newUser("player1", clientHandler);
        gameId = gameSupervisor.newGame(2);
        gameSupervisor.joinGame("player1", gameId);
        assertTrue(gameSupervisor.userIsInGame("player1"));
    }

    @Test
    void notifyDisconnection() {
    }

    @Test
    void gameOver() {
    }

    @Test
    void toJson() throws PlayerIdTakenException, ReachedMaxNumberOfPlayers, NonExistentGameException {
        gameSupervisor.newUser("player1", clientHandler);
        gameSupervisor.newUser("player2", clientHandler2);
        gameId = gameSupervisor.newGame(2);
        gameSupervisor.joinGame("player1", gameId);
        gameSupervisor.joinGame("player2", gameId);

        GameSupervisor gameSupervisor2 = new GameSupervisor(gameSupervisor.toJson());

        assertTrue(gameSupervisor2.equals(gameSupervisor));
    }
}