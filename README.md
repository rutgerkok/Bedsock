# Bedsock

Bedsock is a wrapper for the official Bedrock server software. It reads and writes to its console. The official Bedrock server software is ideal for survival servers, but it lacks a lot of server administration features. This wrapper software makes it possible to add features such as logging and backups using a plugin system. Once the [official Script API](https://minecraft.gamepedia.com/Bedrock_Edition_beta_scripting_documentation) is out, Bedsock aims to form a bridge between the two, so that scripts can do more advanced things such as writing to files and accessing the internet.

## Usage
To start, run `java -jar bedsock.jar path/to/bedrock_server`. By default, the `bedrock_server` file from the current directory is started.

## Program design
First, the program loads all plugins in the `wrapper_plugins` folder. Then, the Bedrock server is started, as well as three threads.

* The main thread ticks every second.
* Another thread reads the commands you're typing. It wakes up the main thread to execute the command.
* A third thread reads what the Bedrock server is doing, and can instruct the main thread to respond on this.

If the thread reading the Bedrock server detects that the Bedrock server is closed, then the main thread is also stopped. This makes the program terminate: the thread reading your commands is a deamon thread, so it is not able to keep the program alive.

Plugins can:

* Register their own console commands (`server.getCommandRegistry()`)
* Listen to events such as the `TickEvent` (`server.getEventRegistry()`)
* Handle logging (`server.getServerLogging().getListeners()`

## Hello world!-plugin
Here is a simple Hello World plugin:

**MyPlugin.java**
```java
package your.package.here;

import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;
import nl.rutgerkok.bedsock.plugin.Plugin;

public class MyPlugin implements Plugin {

    @Override
    public void onEnable(InactiveServer server, ActivePlugin plugin) {
        plugin.getLogger().info("Hello world!");
    }

}
```

**plugin.json**
```json
{
    "name": "MyPlugin",
    "version": "0.1",
    "main": "your.package.here.MyPlugin"
}
```

If you're familiar with Bukkit plugins, this should feel similar. One change is that we use a `plugin.json` file instead of a `plugin.yml` file. Another change is of course that the API is different!
