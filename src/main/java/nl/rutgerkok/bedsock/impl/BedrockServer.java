package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;

final class BedrockServer {

    final BedrockCommandRunner commandRunner;

    BedrockServer(OutputStream serverStdIn) {
        this.commandRunner = new BedrockCommandRunner(serverStdIn);
    }
}
