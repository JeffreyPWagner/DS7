import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

public class PeerHandler extends Thread {
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    private String peerAddress;
    public Socket socket;
    public boolean running;

    public PeerHandler(Socket socket) {
        try {
            this.socket = socket;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            peerAddress = socket.getRemoteSocketAddress().toString();
            peerAddress = peerAddress.substring(1, peerAddress.indexOf(":"));
            ServerMain.handlers.add(this);
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
            running = true;
            while (running) {

                String command = inputStream.readUTF();
                if (command.equalsIgnoreCase("close")) {
                    running = false;
                }
            }
        } catch (Exception e) {
        } finally {
            ServerMain.peers.remove(peerAddress);
            System.out.println(peerAddress + " has disconnected");
        }
    }
}
