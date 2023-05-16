package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.controller.network.rmi.RMIGameInterface;
import it.polimi.ingsw.server.controller.network.rmi.RMILoginInterface;
import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

/**
 * @author Mario Merlo
 */
public class ClientRMI extends Client {
    private RMILoginInterface loginInterface;
    private RMIGameInterface gameInterface;

    public ClientRMI(boolean cli) {
        super(cli);
    }

    @Override
    public void start() throws RemoteException {
        connect();

        login();

        boolean gameOver = false;
        Message reply;
        int headerCode;

        while(!gameOver) {
            reply = gameInterface.ping(getUsername());
            headerCode = reply.getHeaderCode();

            if(headerCode == GET_TILES.getCode())
                getTiles();
            else if(headerCode == GET_COLUMN.getCode())
                getColumn();
            else if(headerCode == GAME_UPDATE.getCode())
                update(reply.getBody().getJSONObject(0));
            else if(headerCode == GAME_OVER.getCode()) {
                gameOver = true;
                gameOver(reply.getBody());
            } else if(headerCode == PING.getCode()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
