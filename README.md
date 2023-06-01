ChatServer.java
Overview
The ChatServer class represents a server for a simple chat application. It allows multiple clients to connect and communicate with each other. The server listens for client connections on a specified port and handles incoming messages from clients.

Dependencies
This class relies on the following Java classes:

java.io.BufferedReader: Reads text from a character input stream.
java.io.IOException: Signals an I/O exception.
java.io.InputStreamReader: Reads bytes and decodes them into characters.
java.io.PrintWriter: Prints formatted representations of objects.
java.net.ServerSocket: Implements a server socket that waits for requests to come in over the network.
java.net.Socket: Represents a socket, which is an endpoint for communication between two machines.
java.time.LocalTime: Represents a time without a date.
Class Members
Constants
DEFAULT_PORT (private, static, final int): The default port number for the server if not specified during initialization.
Fields
clients (private, static, Map<String, PrintWriter>): A map of connected clients, where the key is the client's username, and the value is the output stream to send messages to that client.
Constructors
None
Methods
main(String[] args) throws IOException: The entry point of the ChatServer application. It accepts an optional port number as a command-line argument. It creates a server socket, listens for client connections, and starts a new thread to handle each connected client.
run(): The main logic of the server thread. It handles communication with a single client. It reads messages from the client, processes them according to the specified commands, and sends the appropriate responses to the client.
ChatClient.java
Overview
The ChatClient class represents a client for the chat application. It allows a user to connect to a server, send and receive messages, and perform certain commands.

Dependencies
This class relies on the following Java classes:

java.io.BufferedReader: Reads text from a character input stream.
java.io.IOException: Signals an I/O exception.
java.io.InputStreamReader: Reads bytes and decodes them into characters.
java.io.PrintWriter: Prints formatted representations of objects.
java.net.Socket: Represents a socket, which is an endpoint for communication between two machines.
java.util.Scanner: Allows the user to read values from the console.
Class Members
Constants
DEFAULT_SERVER_ADDRESS (private, static, final String): The default server address (localhost) if not specified during initialization.
DEFAULT_SERVER_PORT (private, static, final int): The default server port number (3000) if not specified during initialization.
Fields
socket (private, static, Socket): The client socket used to connect to the server.
in (private, static, BufferedReader): Reads messages from the server.
out (private, static, PrintWriter): Sends messages to the server.
scanner (private, static, Scanner): Reads user input from the console.
Constructors
None
Methods
main(String[] args) throws IOException: The entry point of the ChatClient application. It accepts an optional server address and port number as command-line arguments. It establishes a connection to the server, reads user input, and sends it to the server.
run(): The main logic of the client thread. It reads messages from the server and prints them to the console.
MessageReader implements Runnable: A nested class that implements the Runnable interface. It is responsible for reading messages from the server in a separate thread to allow simultaneous reading and writing.
