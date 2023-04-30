package it.polimi.ingsw.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class ViewCLI extends View {
    private Client client;

    Scanner scanner = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
    }

    private String composeView(JSONObject gameState) {
        // TODO Implement this method
        return null;
    }

    @Override
    void update() {

    }

    @Override
    String confirmationPrompt(String message) {
        while(true) {
            System.out.print(message);
            String input = scanner.nextLine();
            System.out.print("Are you sure this is ok? (y/n) ");
            String selection = scanner.nextLine();
            if(selection.equals("y"))
                return input;
        }
    }

    @Override
    void okPrompt(String message) {
        System.out.println(message);
        System.out.println();
        System.out.println("Press any key to continue");
        scanner.nextLine();
    }
}
