# UML Diagrams

This document is provided with the UML class diagrams and interaction diagrams in order to make them clearer to the reader.

## Class Diagrams

### Simplified UML
![simplifiedUML](./SimplifiedUML.jpg)

### Specific UML
![specificUML](./SpecificUML.png)

### Model UML
![modelUML](./SpecificModelUML.png)

### Controller UML
![controllerUML](./SpecificControllerUML.png)

### Client UML
![clientUML](./SpecificClientUML.png)

## Interaction Diagrams

### Player Connection

![playerconnection](./PlayerConnection.jpg)

This diagram represents the first connection of a player to the game server.

Once the client is started, it tries to connect to the provided hostname and port to the game server. When the game server accepts that connection, a new `ClientHandler` thread is started, which will then add the user to the list of currently connected users in the `GameSupervisor` class. Once this is done, the client receives a unique `userId`, which will be a hash of the salted player username.

Once the client is connected to the server, two options are available:

- New Game
- Join Game

When creating a new game, the client sends a new game command to the corresponding client handler, then the client handler asks the user how many players will be in that game. Once this information is delivered to the client handler, it creates a new game through the method `newGame` and the game supervisor sets up the game by creating a new `GameController`, which is then assigned a unique `gameId` that is returned to the player.

When joining an existing game, the client asks the client handler what games are currently ongoing on the same server. This information is returned as an array of `gameId`s. The client can then choose among the ongoing games which one to partake in. When this information is sent to the client handler, the `joinGame` method is called on the game supervisor and the game's corresponding game controller is returned to the client handler.

### Player Reconnection

![playerreconnection](./PlayerReconnection.jpg)

When attempting to reconnect to the former game, the player first reconnects to the server. The server hands over the connection to a new instance of `ClientHandler`. The new client handler will ask the client if it wants to log in as a new player or an existing player; if the client chooses to be connected as an existing player, it will then be brought back to its original game. The original game is retrieved by the `GameSupervisor` using the old `userId`.

### Objective completion

![objectivecompletion](./ObjectiveCompletion.jpg)

When the player reaches one of the objectives, the `Game` class in the `server.model` package notifies the game controller, which then sends a message to the `View` in order to let the player know about it.

### Select tiles

![selecttiles](./SelectTiles.jpg)

When starting a turn, the server sends a `GET_TILES` message to the next client that has to play. The client then responds with its selected tiles through a `SEND_TILES` message. Once this message arrives to the server, it checks whether the selected tiles are valid or not. After that, a response is sent to the client accordingly.
