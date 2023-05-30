package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientRMI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.rmi.RMIServer;
import it.polimi.ingsw.server.controller.network.socket.SocketServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;

class Main {
    /**
     * Gets
     * 1) Client or server
     * 2) If client type of communication and GUI or TUI
     * 3) If server port number
     */

    public static void main (String[] args) throws RemoteException {
        System.out.println("Welcome to MyShelfie!!");

        if (args.length == 0){
            // Launch default Configuration
            System.out.println("No arguments provided, launching default configuration: --server --both");
            GameSupervisor gameSupervisor = new GameSupervisor();
            new Thread(gameSupervisor).start();
        } else{
            switch (args[0]) {
                // Client launch
                case "--client", "-c" -> {
                    // Default client launch, socket and GUI
                    if (args.length == 1) new ClientSocket(false).start();
                    else if (args.length == 2) {
                        // Client launch with socket or rmi, GUI as default
                        if (args[1].equals("--socket") || args[1].equals("-s")) new ClientSocket(false).start();
                        else if (args[1].equals("--rmi") || args[1].equals("-r")) new ClientRMI(false).start();
                        else
                            System.out.println("invalid option " + args[1] + "\n" + "To be provided: '--socket/-s' or '--rmi/-r'");
                    } else if (args.length == 3) {
                        // Socket and GUI
                        if ((args[1].equals("--socket") || args[1].equals("-s")) && (args[2].equals("--gui") || args[2].equals("-g")))
                            new ClientSocket(false).start();
                            // Socket and TUI
                        else if ((args[1].equals("--socket") || args[1].equals("-s")) && (args[2].equals("--tui") || args[2].equals("-t")))
                            new ClientSocket(true).start();
                            // RMI and GUI
                        else if ((args[1].equals("--rmi") || args[1].equals("-r")) && (args[2].equals("--gui") || args[2].equals("-g")))
                            new ClientRMI(false).start();
                            // RMI and TUI
                        else if ((args[1].equals("--rmi") || args[1].equals("-r")) && (args[2].equals("--tui") || args[2].equals("-t")))
                            new ClientRMI(true).start();
                        else {
                            System.out.println("Invalid option " + args[1] + ", " + args[2] + " type --help for more information");
                        }
                    }
                }
                // Server launch
                case "--server", "-s" -> {
                    GameSupervisor gameSupervisor = new GameSupervisor();
                    int launchType = 3;
                    int socketPort = 8000;
                    int rmiPort = 1099;
                    for (int i = 1; i < args.length; i++) {
                        if (args[i].equals("--socket") || args[i].equals("-k")) launchType = 1;
                        else if (args[i].equals("--rmi") || args[i].equals("-r")) launchType = 2;
                        else if (args[i].equals("--both") || args[i].equals("-b")) launchType = 3;
                        else if (args[i].contains("--socketPort=") || args[i].contains("-sp=")) {
                            socketPort = Integer.parseInt(args[i].substring(args[i].indexOf("=") + 1));
                        } else if (args[i].contains("--rmiPort=") || args[i].contains("-rp=")) {
                            rmiPort = Integer.parseInt(args[i].substring(args[i].indexOf("=") + 1));
                        } else if (args[i].contains("--path=") || args[i].contains("-p=")) {
                            String path = args[i].substring(args[i].indexOf("=") + 1);
                            try {
                                gameSupervisor = new GameSupervisor(readGameSave(path));
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            System.out.println("Invalid option " + args[i] + ", type --help for more information");
                            return;
                        }
                    }
                    switch (launchType) {
                        case 1 -> new Thread(new SocketServer(socketPort, gameSupervisor)).start();
                        case 2 -> new Thread(new RMIServer(rmiPort, gameSupervisor)).start();
                        case 3 -> {
                            new Thread(new SocketServer(socketPort, gameSupervisor)).start();
                            new Thread(new RMIServer(rmiPort, gameSupervisor)).start();
                        }
                    }
                }
                // Help
                case "--help", "-h" ->
                        System.out.println("Usage: java -jar <fileName.jar> [Launch Type] [Connection type] [args...]\n" +
                                "Launch Type:\n" +
                                "\t --client(-c)/--server(-s)\n" +
                                "Connection type:\n" +
                                "\t --socket(-s)/--rmi(-r)/--both(-b): --both is only valid for server launch\n" +
                                "Arguments:\n" +
                                "\t --gui(-g)/--tui(-t): only valid for client launch\n" +
                                "\t --socketPort(-sp)=<port>: only valid for server launch, if selected when launching in rmi mode only it will be ignored\n" +
                                "\t --rmiPort(-rp)=<port>: only valid for server launch, if selected when launchin in socket mode only it will be ignored \n" +
                                "\t --path(-p)=<path>: only valid for server launch, file must be included in gamesaves directory\n"
                        );
                // Invalid option
                default -> System.out.println("Invalid option " + args[0] + ", type --help/-h for more information");
            }
        }
    }

    private static JSONObject readGameSave(String path) throws IOException {
        // Read a file from path and create a JSONObject with its content
        FileReader fileReader = new FileReader("gameSaves/"+path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return new JSONObject(bufferedReader.readLine());
    }
}