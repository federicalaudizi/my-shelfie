package it.polimi.ingsw.server.controller.network.socket;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is the server of the socket connection
 */
public class SocketServer extends Server {
    private final int port;

    public SocketServer(int port, GameSupervisor ongoingGames) {
        super(ongoingGames);
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started: "+ serverSocket.getInetAddress() +":"+ port);

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
}
