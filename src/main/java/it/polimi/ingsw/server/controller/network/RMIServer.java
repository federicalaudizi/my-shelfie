package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.exceptions.ReachedMaxNumberOfPlayers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer extends Server{

    RMIServer(GameSupervisor ongoingGames) {
        super(ongoingGames);
    }

    // Starts the RMI server
    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            System.out.println("RMI Server started");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
