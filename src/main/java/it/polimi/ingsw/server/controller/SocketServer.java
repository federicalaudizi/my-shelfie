package it.polimi.ingsw.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//This class creates a threaded server that can handle multiple clients
public class SocketServer {
    private final int port;
    private final GameSupervisor ongoingGames;

    public SocketServer(int port) {
        this.port = port;
        this.ongoingGames = new GameSupervisor();
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

                Thread clientThread = new Thread(new ClientHandler(clientSocket, ongoingGames));
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
