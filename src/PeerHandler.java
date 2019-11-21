import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

public class PeerHandler extends Thread {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String peerAddress;

    public PeerHandler(Socket socket) {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            peerAddress = socket.getRemoteSocketAddress().toString();
            peerAddress = peerAddress.substring(1, peerAddress.indexOf(":"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            if (ServerMain.peers.isEmpty()) {
                outputStream.writeUTF("first peer");
                outputStream.flush();
            } else {
                Random randomGen = new Random();
                String neighbor = ServerMain.peers.get(randomGen.nextInt(ServerMain.peers.size()));
                outputStream.writeUTF(neighbor);
                outputStream.flush();
            }
            ServerMain.peers.add(peerAddress);
            boolean running = true;
            while (running) {
                String command = inputStream.readUTF();
                if (command.equalsIgnoreCase("close")) {
                    ServerMain.peers.remove(peerAddress);
                    System.out.println(peerAddress + " has disconnected");
                    running = false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
