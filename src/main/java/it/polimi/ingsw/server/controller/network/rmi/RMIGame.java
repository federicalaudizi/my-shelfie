package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Message;

import java.rmi.RemoteException;

/**
 * This class is the implementation of the RMIGameInterface, each method is called by the remote RMI client and then the call is redirected to the correct RMIClientHandler
 */
public class RMIGame implements RMIGameInterface{
    private final GameSupervisor ongoingGames;

    public RMIGame(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    /**
     * @param username username of the client
     * @return Message containing answer
     * @throws RemoteException if the reference could not be accessed
     * @author Federico
     */
    @Override
    public Message ping(String username) throws RemoteException {
        RMIClientHandler clientHandler = (RMIClientHandler) ongoingGames.getClientHandlerById(username);
        return clientHandler.ping();
    }

    /**
     * @param username username of the client
     * @param tileMessage Message containing the tile
     * @return Message containing answer
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public Message submitTiles(String username, Message tileMessage) throws RemoteException {
        RMIClientHandler clientHandler = (RMIClientHandler) ongoingGames.getClientHandlerById(username);
        return clientHandler.submitTiles(tileMessage);
    }

    /**
     * @param username username of the client
     * @param columnMessage Message containing the column
     * @return Message containing answer
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public Message submitColumn(String username, Message columnMessage) throws RemoteException {
        RMIClientHandler clientHandler = (RMIClientHandler) ongoingGames.getClientHandlerById(username);
        return clientHandler.submitColumn(columnMessage);
    }
}
