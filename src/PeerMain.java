import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PeerMain {
    public static boolean running = true;
    public static List<String> neighbors = new ArrayList<>();
    public static List<NeighborHandler> handlers = new ArrayList<>();
    public static ServerSocket servSock;
    static DataOutputStream outputStream;
    static String connectTo;
    static Socket neighborSock;

    public static void main(String[] args) {
        try {
            servSock = new ServerSocket(33332);
            System.out.println("Peer coming online...");
            PeerInputHandler peerInputHandler = new PeerInputHandler();
            peerInputHandler.start();
            Socket sock = new Socket("35.39.165.63", 33333);
            System.out.println("Connected to central server");
            DataInputStream inputStream = new DataInputStream(sock.getInputStream());
            outputStream = new DataOutputStream(sock.getOutputStream());
            connectTo = inputStream.readUTF();
            System.out.println(connectTo);
            if (!connectTo.equalsIgnoreCase("first peer")) {
                neighborSock = new Socket(connectTo, 33332);
            }
            while (running) {
                Socket socket = servSock.accept();
                NeighborHandler handler = new NeighborHandler(socket);
                handler.start();
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                outputStream.writeUTF("close");
                outputStream.flush();
                if (!connectTo.equalsIgnoreCase("first peer")) {
                    DataOutputStream neighborOutputStream = new DataOutputStream(neighborSock.getOutputStream());
                    neighborOutputStream.writeUTF("disconnecting");
                    outputStream.flush();
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
