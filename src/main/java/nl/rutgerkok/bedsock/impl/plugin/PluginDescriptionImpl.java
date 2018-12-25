package nl.rutgerkok.bedsock.impl.plugin;

import nl.rutgerkok.bedsock.json.ConfigObject;
import nl.rutgerkok.bedsock.json.InvalidConfigException;
import nl.rutgerkok.bedsock.plugin.PluginDescription;

final class PluginDescriptionImpl implements PluginDescription {

    final String mainClass;
    final String name;
    final String version;

    public PluginDescriptionImpl(ConfigObject data) throws InvalidConfigException {
        mainClass = data.getString("main");
        name = data.getString("name");
        version = data.getString("version");
    }

    @Override
    public String getMainClass() {
        return mainClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

}
