import java.util.Scanner;

public class ServerInputHandler extends Thread {

    public void run() {
        try {
            boolean running = true;
            String command;
            Scanner scnr = new Scanner(System.in);
            while (running) {
                command = scnr.nextLine();
                if (command.equalsIgnoreCase("members")) {
                    for (String peer: ServerMain.peers) {
                        System.out.println(peer);
                    }
                } else if (command.equalsIgnoreCase("quit")) {
                    ServerMain.running = false;
                    ServerMain.servSock.close();
                    running = false;
                } else {
                    System.out.println("unknown command");
                }
            }
            scnr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
