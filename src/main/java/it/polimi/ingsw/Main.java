package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Server;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;

class Main {
    /**
     * Gets
     * 1) Client or server
     * 2) If client type of communication and GUI or TUI
     * 3) If server port number
     */

    public static void main (String[] args) {
        System.out.println("Welcome to MyShelfie!!");

        if (args[0].equals("Client")){
            if(args[1].equals("Socket")){
                if (args[2].equals("GUI")){
                    Client c = new ClientSocket(false);
                }else if (args[2].equals("TUI")){
                    Client c = new ClientSocket(true);
                }
            }
        } else if (args[0].equals("server")){
            GameSupervisor gameSupervisor = new GameSupervisor();
            Server s = new SocketServer(Integer.parseInt(args[1]), gameSupervisor);
        }else {
            System.out.println("invalid option -- " + args[0] + "\n" + "To be provided: 'client' or 'server'");
        }
    }
}