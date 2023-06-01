package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.server.controller.network.Server;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ClientSocketTest {
    public static Client client;
    public static Server server;

    @Before
    public void init() {
        client = new ClientSocket(true);
        server = new SocketServer(5000, new GameSupervisor());
    }
}
