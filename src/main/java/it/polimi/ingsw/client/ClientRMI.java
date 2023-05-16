package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author Mario Merlo
 */
public class ClientRMI extends Client {
    public ClientRMI(boolean cli) {
        super(cli);
    }

    @Override
    public void start() {

    }

    @Override
    void connect() throws RemoteException {

    }

    @Override
    void login() throws RemoteException {

    }

    @Override
    void move() throws NullPointerException, UnknownError, RemoteException {

    }

    @Override
    void reconnect() throws RemoteException {

    }

    @Override
    Message getReply() throws NullPointerException {
        return null;
    }

    @Override
    void send(Message message) {

    }
}
