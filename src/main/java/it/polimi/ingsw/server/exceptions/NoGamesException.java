package it.polimi.ingsw.server.exceptions;

public class NoGamesException extends Exception{
    public NoGamesException(String message){
        super(message);
    }
    public NoGamesException(){
        super();
    }
}
