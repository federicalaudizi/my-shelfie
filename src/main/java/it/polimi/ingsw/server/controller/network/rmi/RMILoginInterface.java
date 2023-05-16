package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.network.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMILoginInterface extends Remote {
    Message login(String username) throws RemoteException;
    Message reconnect(String username) throws RemoteException;
    Message createGame(String me, int numberOfPlayers) throws RemoteException;
    Message getGameList(String me) throws RemoteException;
    Message joinGame(String gameID) throws RemoteException;
}
