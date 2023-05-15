package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;

import it.polimi.ingsw.server.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.SynchronousQueue;


// TODO Implement "User has won objective" method

public abstract class View {
    Client client;

    protected static final SynchronousQueue<Object> queue = new SynchronousQueue<>();

    abstract void update(Game game, LinkedList<String> playerOrder);

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);

    abstract void prompt(String message);

    abstract int choicePrompt(String message, String[] options);

    abstract String gameIdSelection(ArrayList<String> gameIds);

    abstract void gameOverScreen(JSONArray leaderboard);
}