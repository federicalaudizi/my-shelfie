package it.polimi.ingsw.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    Socket clientSocket;
    PrintWriter dataOut;
    BufferedReader dataIn;
    Scanner scanner;
    long id;


    public SocketClient(String serverAddress, int serverPort) throws IOException {
        this.clientSocket = new Socket(serverAddress, serverPort);
        this.dataOut = new PrintWriter(clientSocket.getOutputStream(), true);
        this.dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.scanner = new Scanner(System.in);

        System.out.println("Connected to server: " + clientSocket.getInetAddress().getHostName());
    }

    public void newClient() throws IOException {
        dataOut.println("new");
        String incomingId = dataIn.readLine();
        id = Long.parseLong(incomingId);
        System.out.println("My Id is: " + id);
        System.out.println("What do you want to do?");
        System.out.println("1) Create a new game, 2) Join an existing game, 3) Exit");
        String choice = scanner.nextLine();
        dataOut.println(choice);
    }

    public static void main(String[] args) {
        SocketClient client;
        try {
            client = new SocketClient("localhost", 5000);
            client.newClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
