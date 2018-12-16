package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ConsoleReadThread extends Thread {

    private final BufferedReader console;
    private final OutputStreamWriter commandSender;

    public ConsoleReadThread(InputStream console, OutputStream commandSender) {
        this.console = new BufferedReader(new InputStreamReader(console));
        this.commandSender = new OutputStreamWriter(commandSender);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String command = console.readLine() + System.lineSeparator();
                commandSender.write(command);
                commandSender.flush();
            } catch (IOException e) {
                System.out.println("Failed to send command");
                e.printStackTrace();
            }
        }
    }
}
