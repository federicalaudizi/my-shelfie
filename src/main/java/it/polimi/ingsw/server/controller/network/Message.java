package it.polimi.ingsw.server.controller.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class represents a command sent by the server to the client
 * It contains the command code and the arguments of the command
 *
 * @author Federico Liuzzi, Mario Merlo
 */
public class Message implements Serializable {
    private Header header;
    private JSONArray body;

    public Message(String message) throws IOException{
        JSONObject jsonMessage = new JSONObject(message);
        header = Arrays.stream(Header.values()).filter(h -> h.getCode() == jsonMessage.getInt("header")).findFirst().orElseThrow(IOException::new);
        body = jsonMessage.getJSONArray("body");
    }

    public Message(Header header, JSONObject body) {
        this.header = header;
        this.body = new JSONArray();
        this.body.put(body);
    }

    public Message(Header header, JSONArray body) {
        this.header = header;
        this.body = body;
    }

    public Message(Header header) {
        this.header = header;
        body = new JSONArray();
    }

    public int getHeaderCode() {
        return this.header.getCode();
    }

    public JSONArray getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "{" +
               "\"header\":" + header.getCode() + "," +
               "\"body\":" + body.toString() +
               "}";
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeUTF(toString());
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        JSONObject jsonMessage = new JSONObject(in.readUTF());
        header = Arrays.stream(Header.values()).filter(h -> h.getCode() == jsonMessage.getInt("header")).findFirst().orElseThrow(IOException::new);
        body = jsonMessage.getJSONArray("body");
    }

    /**
     * This enum contains all the command codes that the server can send to the client
     * ()Type of command:(1:Information commands; 2:Positive responses; 3:Requests; 4:Errors)                                       
     * ()Context of the command:(1:Login related; 2:Game related)
     *
     * @author Federico
     */
    public enum Header {
        /** Ping message */
        PING(101),

        /** Sent by the server when the game ends, the argument should be the leaderboard */
        GAME_OVER(121),
        /** Sent at the end of every turn in order to let the client update the player's view. */
        GAME_UPDATE(122),


        /** An acknowledgement message that is sent when the contents of a previous response are valid */
        OK(200),

        /** Sent by the server when the player requested to join a game, the argument should be a list of active games */
        GAME_LIST_RESPONSE(211),
        /** Sent by the client when he wants to join an existing game, the argument should be the game id */
        JOIN_GAME_RESPONSE(212),

        /** Sent by the client when he has to select the tiles, the arguments should be the tiles in order of selections */
        SEND_TILES(221),
        /** Sent by the client has to select the column on the shelf, the argument should be the column */
        SEND_COLUMN(222),


        /** Sent by the client at the first login, argument should be the player id */
        LOGIN_REQUEST(311),
        /** Sent by the client at reconnection, argument should be the player id */
        RECONNECT(314),
        /** Sent by the client when he wants to create a new game, the argument should be the number of players */
        NEW_GAME_REQUEST(312),
        /** Sent by the client if he wants to join an existing game */
        JOIN_GAME_REQUEST(313),

        /** sent when the client needs to select the tiles */
        GET_TILES(321),
        /** sent when the client needs to select the column on the shelf */
        GET_COLUMN(322),


        /** Sent by the server when the client sends an invalid command */
        BAD_HEADER(400),

        /** Sent by the server to the client if the chosen username already exists */
        USERNAME_TAKEN(411),
        /** Sent by the server when the client sends an invalid game id */
        BAD_GAME_ID(412),
        /** Sent when there are no available games */
        NO_GAMES(413),
        /** Sent by the server when the client selects invalid tiles */
        BAD_TILES(421),
        /** Sent by the server when the client selects an invalid column on the shelf */
        BAD_COLUMN(422),
        /** Sent by the server when a player tries to reconnect to a game but that game is over. */
        GAME_UNAVAILABLE(423),
        /** Sent by the server when a user tries to reconnect but had never logged in before */
        PLAYER_NOT_FOUND(424);

        private final int code;

        Header(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(code);
        }
    }
}
