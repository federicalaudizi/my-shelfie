# JAR Info

To play a game successfully, you need to start the server first, then the clients.

Please note that Java 19 is required: [Download here](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)

## Server startup
The server can be started with the following command:

`java -jar /path/to/jarfile.jar -s`

By default, a socket server will be started on port 8000 and an RMI server on port 1099.
You can specify whether to start a socket or an RMI on a specific port by providing additional arguments.
You can learn more by typing `java -jar /path/to/jarfile.jar -h`.

## Client startup
The client can be simply started by double-clicking on the JAR file.

Additionally, the client can also be started from the command line with the following command:

`java -jar /path/to/jarfile.jar -c`

By default, the client will launch using socket technology and a GUI.
You can specify whether to use socket or RMI technology and/or a TUI by using providing arguments.
You can learn more by typing `java -jar /path/to/jarfile.jar -h`.