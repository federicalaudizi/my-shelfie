package it.polimi.ingsw.client;

public abstract class View {
    abstract void update();

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);
}