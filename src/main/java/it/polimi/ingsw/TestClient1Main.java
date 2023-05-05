package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientSocket;

public class TestClient1Main {
    public static void main(String[] args) {
        ClientSocket client = new ClientSocket(true);
        try {
            client.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting...");
        }
    }
}
