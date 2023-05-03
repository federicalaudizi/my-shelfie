package it.polimi.ingsw.server.exceptions;

public class PlayerIdTakenException extends Exception{
    public PlayerIdTakenException(){super();}

    public PlayerIdTakenException(String message){super(message);}
}
