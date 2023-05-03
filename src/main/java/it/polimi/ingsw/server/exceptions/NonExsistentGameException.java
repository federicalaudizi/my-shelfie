package it.polimi.ingsw.server.exceptions;

public class NonExsistentGameException extends Exception{
    public NonExsistentGameException(){super();}

    public NonExsistentGameException(String message){super(message);}
}
