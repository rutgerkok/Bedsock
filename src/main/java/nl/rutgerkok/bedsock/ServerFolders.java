package nl.rutgerkok.bedsock;

import java.nio.file.Path;

public interface ServerFolders {

    /**
     * Gets the configuration folder.
     * 
     * @return The configuration folder.
     */
    default Path getConfigFolder() {
        return getRootFolder().resolve("wrapper_config");
    }


    /**
     * Gets the root folder.
     * @return The root folder.
     */
    Path getRootFolder();

    /**
     * Gets the worlds folder.
     * 
     * @return The folder containing all the worlds.
     */
    default Path getWorldsFolder() {
        return getRootFolder().resolve("worlds");
    }
}
