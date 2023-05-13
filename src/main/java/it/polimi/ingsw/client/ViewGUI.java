package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import it.polimi.ingsw.server.model.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewGUI extends View {
    Client client;

    public ViewGUI(Client client) {
        this.client = client;
    }

    @Override
    void update(Game game, LinkedList<String> playerOrder) {

    }

    @Override
    String confirmationPrompt(String message) {
        // TODO Implement
        return null;
    }

    @Override
    void okPrompt(String message) {
        // TODO Implement
    }

    @Override
    void prompt(String message) {
        // TODO Implement
    }

    @Override
    int choicePrompt(String message, String[] options) {
        // TODO Implement
        return 0;
    }

    @Override
    String gameIdSelection(ArrayList<String> gameIds) {
        // TODO Implement
        return null;
    }

    @Override
    void gameOverScreen(HashMap<String, Integer> leaderboard) {
        // TODO Implement
    }
}
