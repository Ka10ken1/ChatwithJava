package src.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static final int DEFAULT_SERVER_PORT = 3000;

    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Scanner scanner;

    public static void main(String[] args) throws IOException {
        String serverAddress = DEFAULT_SERVER_ADDRESS;
        int serverPort = DEFAULT_SERVER_PORT;
        if (args.length > 0) {
            serverAddress = args[0];
            if (args.length > 1) {
                serverPort = Integer.parseInt(args[1]);
            }
        }

        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        out.println(username);

        Thread messageReaderThread = new Thread(new MessageReader());
        messageReaderThread.start();

        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();
            out.println(message);
            if (message.equals("LOGOUT")) {
                break;
            }
        }
        out.close();
        in.close();
        socket.close();
    }
    private static class MessageReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("something went wrong");
                e.printStackTrace();
            }
        }
    }
}
