package src.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int DEFAULT_PORT = 3000;

    private static Map<String, PrintWriter> clients;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);
        clients = new HashMap<>();

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Clients(socket)).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class Clients implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;
        private LocalTime joinTime;
        private static Map<String, LocalTime> clientJoinTimes = new HashMap<>();

        public Clients(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Read the username from the client
                username = in.readLine();
                joinTime = LocalTime.now();
                clients.put(username, out);
                clientJoinTimes.put(username, joinTime);
                for (PrintWriter clientOut : clients.values()) {
                    clientOut.println(username + " has joined the chat!");
                }
                out.println("Possible commands:");
                out.println("@<username> <message> - send a direct message to the specified user");
                out.println("WHOIS - list all connected clients");
                out.println("LOGOUT - log out of the chat");
                out.println("PINGU - send a fun fact about penguins to all clients");
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }

                    if (message.startsWith("@")) {
                        // Direct message to a specific client
                        int index = message.indexOf(" ");
                        String recipient = message.substring(1, index);
                        PrintWriter recipientOut = clients.get(recipient);
                        if (recipientOut == null) {
                            out.println("Error: no client with username '" + recipient + "'");
                        } else {
                            recipientOut.println(username + ": " + message.substring(index + 1));
                        }
                    } else if (message.equals("WHOIS")) {
                        out.println("Connected clients:");
                        for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
                            String clientUsername = entry.getKey();
                            LocalTime clientJoinTime = clientJoinTimes.get(clientUsername);
                            out.println(clientUsername + " (joined at " + clientJoinTime + ")");
                        }
                    } else if (message.equals("LOGOUT")) {
                        clients.remove(username);
                        clientJoinTimes.remove(username);
                        break;
                    } else if (message.equals("PINGU")) {
                        for (PrintWriter clientOut : clients.values()) {
                            clientOut.println("Did you know that penguins can swim 15mph?");
                        }
                    } else {
                        for (PrintWriter clientOut : clients.values()) {
                            clientOut.println(username + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("something went wrong");
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("something went wrong");
                    e.printStackTrace();
                }
            }
        }
    }
}
