package it.polimi.ingsw.server.exceptions;

public class PlayerDisconnectedException extends Exception{
    public PlayerDisconnectedException(String message){
        super(message);
    }
    public PlayerDisconnectedException(){
        super();
    }
}
