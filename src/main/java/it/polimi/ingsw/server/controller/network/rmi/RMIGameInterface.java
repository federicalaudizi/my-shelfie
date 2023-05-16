package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.network.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGameInterface extends Remote {
    Message ping(String username) throws RemoteException;
    Message submitTiles(String username, Message tileMessage) throws RemoteException;
    Message submitColumn(String username, Message columnMessage) throws RemoteException;
}
