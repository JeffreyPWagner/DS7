package server;

import java.net.*;
import java.util.*;

public class ServerMain {
    public static boolean running = true;
    public static List<String> peers = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket servSock = new ServerSocket(33333);
            System.out.println("The server is on!");
            ServerInputHandler serverInputHandler = new ServerInputHandler();
            serverInputHandler.start();
            while (running) {
                Socket socket = servSock.accept();
                PeerHandler handler = new PeerHandler(socket);
                handler.start();
                peers.add(socket.getRemoteSocketAddress().toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
