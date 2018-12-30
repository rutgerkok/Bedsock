package nl.rutgerkok.bedsock.config;

/**
 * Thrown when an invalid configuration file is found.
 *
 */
public final class InvalidConfigException extends Exception {

    private static final long serialVersionUID = -539189975123194325L;

    public InvalidConfigException(String reason) {
        super(reason);
    }

}
