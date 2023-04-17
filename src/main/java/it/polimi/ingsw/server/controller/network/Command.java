package it.polimi.ingsw.server.controller.network;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * This class represents a command sent by the server to the client
 * It contains the command code and the arguments of the command
 *
 * @author Federico
 */
class Command {
    private final CommandCode code;
    private final JSONObject[] args;

    Command(CommandCode code, JSONObject[] args) {
        this.code = code;
        this.args = args;
    }

    Command(CommandCode code, JSONObject arg) {
        this.code = code;
        this.args = new JSONObject[]{arg};
    }

    Command(CommandCode code) {
        this.code = code;
        this.args = new JSONObject[]{};
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append("{\"code\": \"").append(code).append("\", \"args\": [");
        for(JSONObject arg : args){
            ret.append(arg.toString()).append(", ");
        }
        ret.append("]}");

        return ret.toString();
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
    enum CommandCode {
        /** sent when the client connects to the server */
        WELCOME("101"),

        /** sent when the game ends, the argument should be the winner's id, arguments should be the leaderboard */
        GAME_OVER("121"),


        /** sent when the client sends a valid command, there could be an argument if needed*/
        OK("200"),
        /** sent when the player selects the player id, the argument should be the player id */
        NEW_PLAYER_ID_RESPONSE("212"),
        /** sent when the player selects the player id, the argument should be the player id */
        OLD_PLAYER_ID_RESPONSE("213"),
        /** sent when the player selects the game id, the argument should be the game id */
        GAME_ID_RESPONSE("214"),
        /** sent when the player wants to create a new game, there should be no arguments*/
        NEW_GAME_RESPONSE("215"),
        /** sent when the player wants to join an existing game, there should be no arguments*/
        JOIN_GAME_RESPONSE("216"),

        /** sent when the client has to select the tiles, the arguments should be the tiles in order of selections */
        TILES_RESPONSE("221"),
        /** sent when the client has to select the column on the shelf, the argument should be the column */
        COLUMN_RESPONSE("222"),


        /** Sent when the client has to log in */
        LOGIN_REQUEST("311"),
        /** Sent when the client has to select whether to create a new game or join an existing one, no arguments needed */
        GAME_CREATION_REQUEST("312"),
        /** sent when the client has to select a game id, the arguments of this command should be the game ids */
        GAME_ID_REQUEST("313"),

        /** sent when the client needs to update the game state*/
        GAME_STATE_REQUEST("321"),
        /** sent when the client needs to select the tiles */
        TILES_REQUEST("322"),
        /** sent when the client needs to select the column on the shelf */
        COLUMN_REQUEST("323"),
        /** sent by the server when the client has to update its game state, the argument should be the game state JSON object */
        VIEW_UPDATE_REQUEST("324"),


        /** sent when the client sends an invalid command */
        GENERIC_ERROR("400"),

        /** sent when the client sends an invalid player id */
        BAD_PLAYER_ID_ERROR("411"),
        /** sent when the client sends an invalid game id */
        BAD_GAME_ID_ERROR("412"),
        /** Sent when the client selects invalid tiles */
        BAD_TILES_ERROR("421"),
        /** sent when the client selects an invalid column on the shelf */
        BAD_COLUMN_ERROR("422"),
        ;

        private final String code;

        CommandCode(String code) {
            this.code = code;
        }

        public String toString(){
            return this.code;
        }
    }
}
