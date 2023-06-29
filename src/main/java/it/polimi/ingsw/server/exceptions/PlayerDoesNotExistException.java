package it.polimi.ingsw.server.exceptions;

public class PlayerDoesNotExistException extends Exception{
    public PlayerDoesNotExistException(){super();}

    public PlayerDoesNotExistException(String message){super(message);}
}
