package nl.rutgerkok.bedsock.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import nl.rutgerkok.bedsock.SockLogger;
import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.command.CommandRunner;

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
        SockLogger logger = new PrintlnLogger();

        File file = getExecutableFile(logger);
        if (file == null) {
            return null;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(file.toString());
        processBuilder.directory(file.getParentFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        InputStream outputOfProcess = process.getInputStream();
        BedrockServer server = new BedrockServer(process.getOutputStream());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (process.isAlive()) {
                    logger.warning(
                            "Wrapper was forcibly closed, but Bedrock server was still active. Closing it now...");
                    try {
                        server.commandRunner.runCommand("stop");
                        if (!process.waitFor(5, TimeUnit.SECONDS)) {
                            process.destroyForcibly(); // Ok, failed
                        }
                    } catch (CommandException | InterruptedException e) {
                        process.destroyForcibly(); // Ok, failed
                    }
                }
            }
        });

        startConsoleReadThread(server.commandRunner, logger);
        passOutput(process, outputOfProcess);

        return null;
    }

    private File getExecutableFile(SockLogger logger) {
        File file = this.file.getAbsoluteFile();

        if (System.getProperty("os.name").contains("Windows") && !file.toString().endsWith(".exe")) {
            file = new File(file + ".exe");
        }
        if (!file.exists()) {
            logger.error("Bedrock server not found! We searched for it at " + file);
            return null;
        }
        return file;
    }

    private void passOutput(Process process, InputStream outputOfProcess) throws IOException {
        byte[] buffer = new byte[256];
        while (process.isAlive()) {
            int length = outputOfProcess.read(buffer);
            if (length > 0) {
                System.out.write(buffer, 0, length);
            }
        }
    }

    private void startConsoleReadThread(CommandRunner commandRunner, SockLogger logger) {
        ConsoleReadThread consoleReadThread = new ConsoleReadThread(System.in, commandRunner, logger);
        consoleReadThread.setDaemon(true);
        consoleReadThread.start();
    }
}