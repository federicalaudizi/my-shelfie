package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends Server {

    /**
     * Creates a new instance of the server on the default port
     * @param ongoingGames the GameSupervisor instance
     * @author Federico
     */
    public RMIServer(GameSupervisor ongoingGames) {
        super(1099, ongoingGames);
    }

    /**
     * Creates a new instance of the server on the specified port
     * @param port the port on which the server will be listening
     * @param ongoingGames the GameSupervisor instance
     * @author Federico
     */
    public RMIServer(int port, GameSupervisor ongoingGames) {
        super(port, ongoingGames);
    }

    // Starts the RMI server
    @Override
    public void run() {
        try {

            RMILogin login = new RMILogin(ongoingGames);
            RMIGame game = new RMIGame(ongoingGames);

            RMILoginInterface loginSkeleton = (RMILoginInterface) UnicastRemoteObject.exportObject(login, 0);
            RMIGameInterface gameSkeleton = (RMIGameInterface) UnicastRemoteObject.exportObject(game, 0);

            Registry registry = LocateRegistry.createRegistry(port);

            registry.bind("RMILoginInterface", loginSkeleton);
            registry.bind("RMIGameInterface", gameSkeleton);

            System.out.println("RMI server started on port " + port);
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}
