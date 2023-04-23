package it.polimi.ingsw.server.controller.network;

import org.json.JSONArray;

/**
 * This class represents a command sent by the server to the client
 * It contains the command code and the arguments of the command
 *
 * @author Federico
 */
public class Message {
    private final Header header;
    private final JSONArray body;

    public Message(Header header, JSONArray body) {
        this.header = header;
        this.body = body;
    }

    public Message(Header header) {
        this.header = header;
        body = new JSONArray();
    }

    @Override
    public String toString() {
        return "{\n" +
               "\t\"header\": " + header.toString() + ",\n" +
               "\t\"body\": " + body.toString() + "\n" +
               "}";
    }

    /**
     * This enum contains all the command codes that the server can send to the client
     * @FirstDigit: Type of command
     * @1: Information commands
     * @2: Positive responses
     * @3: Requests
     * @4: Errors
     *
     * @SecondDigit Context of the command
     * @1: Login related
     * @2: Game related
     *
     * @author Federico
     */
    public enum Header {
        /** Welcome Command code */
        WELCOME(101),

        /** Sent by the server when the game ends, the argument should be the leaderboard */
        GAME_OVER(121),
        /** Sent at the end of every turn in order to let the client update the player's view. */
        GAME_UPDATE(122),


        /** An acknowledgement message that is sent when the contents of a previous response are valid */
        OK(200),

        /** Sent by the server when the player requested to join a game, the argument should be a list of active games */
        GAMES_ID_RESPONSE(211),
        /** Sent by the client when he wants to join an existing game, the argument should be the game id */
        JOIN_GAME_RESPONSE(211),

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
        GENERIC_ERROR(400),

        /** Sent by the server to the client if the chosen username already exists */
        USERNAME_TAKEN(411),
        /** Sent by the server when the client sends an invalid game id */
        BAD_GAME_ID(412),
        /** Sent by the server when the client selects invalid tiles */
        BAD_TILES(421),
        /** Sent by the server when the client selects an invalid column on the shelf */
        BAD_COLUMN(422),
        ;

        private final int code;

        Header(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
