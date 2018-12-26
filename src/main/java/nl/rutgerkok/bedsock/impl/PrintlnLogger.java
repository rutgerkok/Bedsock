package nl.rutgerkok.bedsock.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import nl.rutgerkok.bedsock.Logger;

/**
 * Ultra-simple logger.
 */
public final class PrintlnLogger implements Logger {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void chat(String name, String message) {
        System.out.println("[Sock] " + name + ": " + message);
    }

    @Override
    public void error(String message, Throwable e) {
        error(message);
        e.printStackTrace();
    }

    @Override
    public void log(LogLevel level, String message) {
        System.out.println("[" + timeFormat.format(new Date()) + " " + level.name() + "] " + message);
    }


}
