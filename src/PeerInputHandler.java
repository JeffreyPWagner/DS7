import java.util.Scanner;

public class PeerInputHandler extends Thread {

    public void run() {
        try {

                boolean running = true;
                String command;
                Scanner scnr = new Scanner(System.in);
                while (running) {
                    command = scnr.nextLine();
                    if (command.equalsIgnoreCase("neighbors")) {
                        for (String neighbor : PeerMain.neighbors) {
                            System.out.println(neighbor);
                        }
                    } else if (command.equalsIgnoreCase("quit")) {
                        PeerMain.outputStream.writeUTF("close");
                        PeerMain.outputStream.flush();
                        sleep(500);
                        System.exit(0);
                        PeerMain.running = false;
                        PeerMain.servSock.close();
                        running = false;
                    } else {
                        System.out.println("unknown command");
                    }
                }
                scnr.close();

        } catch (Exception e) {
        }
    }
}
