package nl.rutgerkok.bedsock;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(description = "Starts the server wrapper", name = "start", mixinStandardHelpOptions = true, version = "0.0.1")
public class StartServer implements Callable<Void> {

    public static void main(String[] args) throws Exception {
        CommandLine.call(new StartServer(), args);
    }

    @Parameters(index = "0", description = "The file to run.", defaultValue = "bedrock_server")
    private File file;

    @Override
    public Void call() throws Exception {
        File file = this.file.getAbsoluteFile();
        ProcessBuilder processBuilder = new ProcessBuilder(file.toString());
        processBuilder.directory(file.getParentFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStream outputOfProcess = process.getInputStream();
        OutputStream commandSender = process.getOutputStream();

        ConsoleReadThread consoleReadThread = new ConsoleReadThread(System.in,
                commandSender);
        consoleReadThread.setDaemon(true);
        consoleReadThread.start();

        byte[] buffer = new byte[256];
        while (process.isAlive()) {
            int length = outputOfProcess.read(buffer);
            if (length > 0) {
                System.out.write(buffer, 0, length);
            }
        }

        System.out.println("Wrapper shutting down...");
        return null;
    }
}