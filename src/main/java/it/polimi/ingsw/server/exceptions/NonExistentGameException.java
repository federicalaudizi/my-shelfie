package it.polimi.ingsw.server.exceptions;

public class NonExistentGameException extends Exception{
    public NonExistentGameException(){super();}

    public NonExistentGameException(String message){super(message);}
}
