package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Message;

import java.rmi.RemoteException;

public class RMIGame implements RMIGameInterface{
    GameSupervisor ongoingGames;

    public RMIGame(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    /**
     * @param username
     * @return
     * @throws RemoteException
     */
    @Override
    public Message ping(String username) throws RemoteException {
        RMIClientHandler clientHandler = (RMIClientHandler) ongoingGames.getClientHandlerById(username);
        return null;
    }

    /**
     * @param username
     * @param tileMessage
     * @return
     * @throws RemoteException
     */
    @Override
    public Message submitTiles(String username, Message tileMessage) throws RemoteException {
        return null;
    }

    /**
     * @param username
     * @param columnMessage
     * @return
     * @throws RemoteException
     */
    @Override
    public Message submitColumn(String username, Message columnMessage) throws RemoteException {
        return null;
    }
}
