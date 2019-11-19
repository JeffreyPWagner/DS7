package peer;

import server.PeerHandler;
import server.ServerInputHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PeerMain {
    public static boolean running = true;
    public static List<String> neighbors = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket servSock = new ServerSocket(33333);
            System.out.println("Peer coming online...");
            PeerInputHandler peerInputHandler = new PeerInputHandler();
            peerInputHandler.start();
            Socket sock = new Socket("35.40.171.8", 44444);
            DataInputStream inputStream = new DataInputStream(sock.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(sock.getOutputStream());
            String connectTo = inputStream.readUTF();
            if (!connectTo.equalsIgnoreCase("first peer")) {
                Socket neighborSock = new Socket(connectTo, 44444);
            }
            while (running) {
                Socket socket = servSock.accept();
                NeighborHandler handler = new NeighborHandler(socket);
                handler.start();
                neighbors.add(socket.getRemoteSocketAddress().toString());
            }
            outputStream.writeUTF("close");
            outputStream.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
