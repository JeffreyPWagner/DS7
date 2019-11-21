import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PeerMain {
    public static boolean running = true;
    public static List<String> neighbors = new ArrayList<>();
    public static List<NeighborHandler> handlers = new ArrayList<>();
    public static ServerSocket servSock;
    static DataOutputStream outputStream;
    public static String connectTo;
    public static Socket neighborSock = null;
    static NeighborClientHandler neighborClient;
    public static final Object lock = new Object();

    public static void main(String[] args) {
        try {
            System.out.println("Enter server IP");
            Scanner scnr = new Scanner(System.in);
            String centralIP = scnr.nextLine();
            servSock = new ServerSocket(33332);
            System.out.println("Peer coming online...");
            PeerInputHandler peerInputHandler = new PeerInputHandler();
            peerInputHandler.start();
            Socket sock = new Socket(centralIP, 33333);
            System.out.println("Connected to central server");
            DataInputStream inputStream = new DataInputStream(sock.getInputStream());
            outputStream = new DataOutputStream(sock.getOutputStream());
            connectTo = inputStream.readUTF();
            if (!connectTo.equalsIgnoreCase("first peer")) {
                synchronized (lock) {
                    while (neighborSock == null) {
                        System.out.println("Attempting to connect to: " + connectTo);
                        neighborSock = new Socket(connectTo, 33332);
                        neighborClient = new NeighborClientHandler(neighborSock);
                        neighborClient.start();
                        lock.wait();
                    }
                }
                System.out.println("connection successful");
            } else {
                System.out.println("I am the first peer");
            }
            while (running) {
                Socket socket = servSock.accept();
                NeighborHandler handler = new NeighborHandler(socket);
                handler.start();
            }

        } catch (Exception e) {
        } finally {
            try {
                outputStream.writeUTF("close");
                outputStream.flush();
                if (!connectTo.equalsIgnoreCase("first peer") && neighborClient.isAlive()) {
                    neighborClient.disconnect();
                    neighborClient.interrupt();
                }
                for (NeighborHandler h : handlers) {
                    h.disconnect();
                    h.interrupt();
                }
            } catch (Exception e) {
            }
        }
    }
}
