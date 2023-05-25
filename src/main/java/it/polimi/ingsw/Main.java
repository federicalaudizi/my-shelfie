package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientRMI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.rmi.RMIServer;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;

class Main {
    /**
     * Gets
     * 1) Client or server
     * 2) If client type of communication and GUI or TUI
     * 3) If server port number
     */

    public static void main (String[] args) throws Exception {
        System.out.println("Welcome to MyShelfie!!");

        if (args[0].equals("client")){
            Client c;
            if(args[1].equals("socket")){
                if (args[2].equals("gui")){
                    //Image image = new Image("file:src/main/resources/Images/eight.jpg");
                    c = new ClientSocket(false);
                }else if (args[2].equals("tui")){
                    c = new ClientSocket(true);
                }else {
                    System.out.println("invalid option -- " + args[2] + "\n" + "To be provided: 'gui' or 'tui'");
                    return;
                }
            } else if (args[1].equals("rmi")){
                if (args[2].equals("gui")){
                    c = new ClientRMI(false);
                }else if (args[2].equals("tui")){
                    c = new ClientRMI(true);
                } else {
                    System.out.println("invalid option -- " + args[2] + "\n" + "To be provided: 'gui' or 'tui'");
                    return;
                }
            } else {
                System.out.println("invalid option -- " + args[1] + "\n" + "To be provided: 'socket' or 'rmi'");
                return;
            }
            c.start();
        } else if (args[0].equals("server")){
            GameSupervisor gameSupervisor = new GameSupervisor();
            new Thread(gameSupervisor).start();
            switch (args[1]) {
                case "socket" -> new Thread(new SocketServer(8000, gameSupervisor)).start();
                case "rmi" -> new Thread(new RMIServer(1099, gameSupervisor)).start();
                case "both" -> {
                    new Thread(new SocketServer(8000, gameSupervisor)).start();
                    new Thread(new RMIServer(1099, gameSupervisor)).start();
                }
                default ->
                        System.out.println("invalid option -- " + args[1] + "\n" + "To be provided: 'socket' or 'rmi' or 'both'");
            }
        }else {
            System.out.println("invalid option -- " + args[0] + "\n" + "To be provided: 'client' or 'server'");
        }
    }
}