import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    public static boolean running = true;
    public static List<String> peers = new ArrayList<>();
    public static ServerSocket servSock;

    public static void main(String[] args) {
        try {
            servSock = new ServerSocket(33333);
            System.out.println("The server is on!");
            ServerInputHandler serverInputHandler = new ServerInputHandler();
            serverInputHandler.start();
            while (running) {
                System.out.println("Listening for peer...");
                Socket socket = servSock.accept();
                System.out.println("peer connected");
                PeerHandler handler = new PeerHandler(socket);
                handler.start();
            }
        } catch (Exception e) {
        }
    }
}
