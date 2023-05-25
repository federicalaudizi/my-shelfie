package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameSupervisor gameSupervisor;
    GameController gameController1;
    GameController gameController2;

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
    void toJson() {
    }

    @Test
    void getPlayerToClientHandlerMap() {
    }

    @Test
    void getConnectedPlayers() {
    }
}