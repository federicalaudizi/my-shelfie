package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends Server {

    RMIServer(GameSupervisor ongoingGames) {
        super(ongoingGames);
    }

    // Starts the RMI server
    @Override
    public void run() {
        try {

            RMILogin login = new RMILogin(ongoingGames);
            RMIGame game = new RMIGame();

            RMILoginInterface loginSkeleton = (RMILoginInterface) UnicastRemoteObject.exportObject(login, 0);
            RMIGameInterface gameSkeleton = (RMIGameInterface) UnicastRemoteObject.exportObject(game, 0);

            Registry registry = LocateRegistry.getRegistry();

            registry.bind("RMILoginInterface", loginSkeleton);
            registry.bind("RMIGameInterface", gameSkeleton);

            System.out.println("Server ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}
