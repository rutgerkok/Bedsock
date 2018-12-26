package nl.rutgerkok.bedsock.plugin;

import nl.rutgerkok.bedsock.Logger;

public interface ActivePlugin {

    Plugin getPlugin();

    PluginDescription getPluginDescription();

    Logger getPluginLogger();

}
