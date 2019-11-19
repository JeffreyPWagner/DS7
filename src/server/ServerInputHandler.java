package server;

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
