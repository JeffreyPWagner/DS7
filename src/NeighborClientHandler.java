import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

public class NeighborClientHandler extends Thread {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String neighborAddress;
    private Socket socket;
    boolean running = true;

    public NeighborClientHandler(Socket socket) {
        try {
            this.socket = socket;
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            neighborAddress = socket.getRemoteSocketAddress().toString();
            neighborAddress = neighborAddress.substring(1, neighborAddress.indexOf(":"));
            PeerMain.neighbors.add(neighborAddress);
        } catch (Exception e) {
        }
    }

    public void disconnect() {
        try {
            outputStream.writeUTF("disconnecting");
            outputStream.flush();
            socket.close();
        } catch (Exception e) {
        }
    }

    public void run() {
        try {
            synchronized (PeerMain.lock) {

                String response = inputStream.readUTF();
                if (!response.equalsIgnoreCase("confirmed")) {
                    PeerMain.neighbors.remove(neighborAddress);
                    PeerMain.neighborSock = null;
                    PeerMain.connectTo = response;
                    PeerMain.lock.notifyAll();
                } else {
                    PeerMain.lock.notifyAll();
                }
            }
            while (running) {
                String notification = inputStream.readUTF();
                if (notification.equalsIgnoreCase("disconnecting")) {
                    PeerMain.neighbors.remove(neighborAddress);
                    System.out.println(neighborAddress + " has disconnected");
                    running = false;
                }
            }


        } catch (Exception e) {
        } finally {
            PeerMain.neighbors.remove(neighborAddress);
            System.out.println(neighborAddress + " has disconnected");
        }
    }
}
