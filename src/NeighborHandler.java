import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

public class NeighborHandler extends Thread {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String neighborAddress;

    public NeighborHandler(Socket socket) {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            neighborAddress = socket.getRemoteSocketAddress().toString();
            neighborAddress = neighborAddress.substring(1, neighborAddress.indexOf(":"));
            PeerMain.neighbors.add(neighborAddress);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void disconnect() {
        try {
            outputStream.writeUTF("disconnecting");
            outputStream.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            if (PeerMain.neighbors.size() > 3) {
                PeerMain.neighbors.remove(neighborAddress);
                Random randomGen = new Random();
                String neighbor = PeerMain.neighbors.get(randomGen.nextInt(PeerMain.neighbors.size()));
                outputStream.writeUTF(neighbor);
                outputStream.flush();
            } else {
                PeerMain.handlers.add(this);
                boolean running = true;
                while (running) {
                    String notification = inputStream.readUTF();
                    if(interrupted()){
                        break;
                    }
                    if (notification.equalsIgnoreCase("disconnecting")) {
                        PeerMain.neighbors.remove(neighborAddress);
                        System.out.println(neighborAddress + " has disconnected");
                        running = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
