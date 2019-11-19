package peer;

import server.ServerMain;

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
                    PeerMain.running = false;
                    running = false;
                } else {
                    System.out.println("unknown command");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
