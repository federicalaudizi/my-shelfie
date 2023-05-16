package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.socket.SocketClientHandler;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketClientHandlerTest extends TestCase {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ClientHandler clientHandler;
    private GameSupervisor games;
    private PrintWriter clientHandlerInput;
    private BufferedReader clientHandlerOutput;
    private Thread clientHandlerThread;
    private Message response;


    public void setUp() throws Exception {
        games = new GameSupervisor();

        serverSocket = new ServerSocket(5000);
        clientSocket = new Socket("localhost", 5000);

        clientHandlerInput = new PrintWriter(clientSocket.getOutputStream(), true);
        clientHandlerOutput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Socket clientHandlerSocket = serverSocket.accept();
        clientHandler = new SocketClientHandler(clientHandlerSocket, games);
        clientHandlerThread = new Thread(clientHandler);
    }

    public void testTestRun() throws Exception {
        /*
        clientHandlerThread.start();

        // Test first time login
        clientHandlerInput.println(new Message(LOGIN_REQUEST, new JSONObject().put("username", "testUser1")));
        response = new Message(clientHandlerOutput.readLine());
        assert(response.getHeaderCode() == OK.getCode());
        assert(games.userExists("testUser1"));

        tearDown();
        setUp();

        // Test reconnection
        clientHandlerInput.println(new Message(RECONNECT, new JSONObject().put("username", "testUser1")));
        response = new Message(clientHandlerOutput.readLine());
        assert(response.getHeaderCode() == GENERIC_ERROR.getCode());

        */
    }

    public void testSendGameState() {
    }

    public void testGetTiles() {
    }

    public void testGameOver() {
    }

    @Override
    public void tearDown() throws Exception {
        clientHandlerThread.interrupt();
        clientSocket.close();
        serverSocket.close();
    }
}