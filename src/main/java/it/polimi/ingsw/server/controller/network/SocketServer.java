package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisorString;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is the server of the socket connection
 */
public class SocketServer extends Server{
    private final int port;
    private final GameSupervisorString ongoingGames;

    public SocketServer(int port) {
        this.port = port;
        this.ongoingGames = new GameSupervisorString();
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

                Thread clientThread = new Thread(new SocketClientHandler(clientSocket, ongoingGames));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer(5000);
        server.startServer();
    }
}
