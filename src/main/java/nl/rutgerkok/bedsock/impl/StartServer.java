package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;

import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.logger.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(description = "Starts the server wrapper", name = "start", mixinStandardHelpOptions = true, version = "0.0.1")
public class StartServer implements Callable<@Nullable Void> {

    private static final boolean ON_MS_WINDOWS = System.getProperty("os.name").contains("Windows");

    public static void main(String[] args) throws Exception {
        CommandLine.call(new StartServer(), args);
    }

    @Parameters(index = "0", description = "The file to run.", defaultValue = "bedrock_server")
    private File file = new File("bedrock_server");

    @Option(names = { "--plugin_folder" }, description = "The folder where all plugins are stored")
    private File pluginFolder = new File("wrapper_plugins");

    @Override
    public @Nullable Void call() throws Exception {
        InactiveServerImpl inactiveServer = new InactiveServerImpl();
        inactiveServer.getServerLogger().info("Starting server wrapper...");
        inactiveServer.pluginRegistry.loadPlugins(inactiveServer, pluginFolder.toPath());
        inactiveServer.pluginRegistry.enablePlugins(inactiveServer);

        File file = getExecutableFile(inactiveServer.getServerLogger());
        if (file == null) {
            return null;
        }

        inactiveServer.getServerLogger().info("Starting Bedrock server...");
        Process process = startServer(file);

        BufferedReader outputOfProcess = new BufferedReader(new InputStreamReader(process.getInputStream()));
        ActiveServerImpl server = new ActiveServerImpl(process.getOutputStream(), inactiveServer);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (process.isAlive()) {
                    server.getServerLogger().warning(
                            "Wrapper was forcibly closed, but Bedrock server was still active. Closing it now...");
                    try {
                        server.getBedrockCommandRunner().runCommand("stop");
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
        BedrockReaderThread bedrockReadThread = new BedrockReaderThread(outputOfProcess, server);
        consoleReadThread.setDaemon(true);
        consoleReadThread.start();
        bedrockReadThread.start();

        server.getScheduler().mainloop(server);

        return null;
    }

    private @Nullable File getExecutableFile(Logger logger) {
        File file = this.file.getAbsoluteFile();

        if (ON_MS_WINDOWS && !file.toString().endsWith(".exe")) {
            file = new File(file + ".exe");
        }
        if (!file.exists()) {
            logger.error("Bedrock server not found! We searched for it at " + file);
            return null;
        }
        return file;
    }

    private Process startServer(File file) throws IOException {
        String fileName = ON_MS_WINDOWS ? file.toString() : "./" + file.getName();
        ProcessBuilder processBuilder = new ProcessBuilder(fileName);
        processBuilder.directory(file.getParentFile());
        processBuilder.environment().put("LD_LIBRARY_PATH", ".");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return process;
    }
}