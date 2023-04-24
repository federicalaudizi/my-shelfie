package it.polimi.ingsw.server.exceptions;

public class PlayerDoesNotExistsException extends Exception{
    public PlayerDoesNotExistsException(){super();}

    public PlayerDoesNotExistsException(String message){super(message);}
}
