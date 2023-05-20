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

    /**
     * Creates a new instance of the server on the default 5000 port
     * @param ongoingGames the GameSupervisor instance
     * @author Federico
     */
    public SocketServer(GameSupervisor ongoingGames) {
        super(5000, ongoingGames);
    }

    /**
     * Creates a new instance of the server on the specified port
     * @param port the port on which the server will be listening
     * @param ongoingGames the GameSupervisor instance
     * @author Federico
     */
    public SocketServer(int port, GameSupervisor ongoingGames) {
        super(port, ongoingGames);
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
