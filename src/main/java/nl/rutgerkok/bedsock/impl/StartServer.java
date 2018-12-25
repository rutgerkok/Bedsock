package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.command.CommandException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(description = "Starts the server wrapper", name = "start", mixinStandardHelpOptions = true, version = "0.0.1")
public class StartServer implements Callable<Void> {

    public static void main(String[] args) throws Exception {
        CommandLine.call(new StartServer(), args);
    }

    @Parameters(index = "0", description = "The file to run.", defaultValue = "bedrock_server")
    private File file;

    @Option(names = {
            "--plugin_folder" }, description = "The folder where all plugins are stored", defaultValue = "wrapper_plugins")
    private File pluginFolder;

    @Override
    public Void call() throws Exception {
        Logger logger = new PrintlnLogger();

        File file = getExecutableFile(logger);
        if (file == null) {
            return null;
        }

        Process process = startServer(file);

        BufferedReader outputOfProcess = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BedrockServer server = new BedrockServer(process.getOutputStream(), logger);

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

        ConsoleReadThread consoleReadThread = new ConsoleReadThread(System.in, server);
        BedrockReaderThread bedrockReadThread = new BedrockReaderThread(outputOfProcess, logger);
        consoleReadThread.setDaemon(true);
        consoleReadThread.start();
        bedrockReadThread.start();

        server.pluginLoader.loadPlugins(pluginFolder.toPath());
        server.pluginLoader.enablePlugins(server);

        return null;
    }

    private File getExecutableFile(Logger logger) {
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

    private Process startServer(File file) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(file.toString());
        processBuilder.directory(file.getParentFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return process;
    }
}