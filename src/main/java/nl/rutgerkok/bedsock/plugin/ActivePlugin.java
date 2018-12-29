package nl.rutgerkok.bedsock.plugin;

import nl.rutgerkok.bedsock.logger.Logger;

public interface ActivePlugin {

    Plugin getPlugin();

    PluginDescription getPluginDescription();

    Logger getLogger();

}
