package nl.rutgerkok.bedsock.impl;

import nl.rutgerkok.bedsock.SockLogger;

/**
 * Ultra-simple logger.
 */
final class PrintlnLogger implements SockLogger {

    @Override
    public void chat(String name, String message) {
        System.out.println("[Sock] " + name + ": " + message);
    }

    @Override
    public void debug(String message) {
        System.out.println("[Debug] " + message);
    }

    @Override
    public void info(String message) {
        System.out.print("[Info] " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("[Severe]" + message);
    }

    @Override
    public void warning(String message) {
        System.out.println("[Warning] " + message);
    }

}
