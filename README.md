# Bedsock

Wrapper for the official Minecraft Bedrock server software. Only tested on Windows for now.

To start, run `java -jar bedsock.jar path/to/bedrock_server`. By default, the `bedrock_server` file from the current directory is started.

## Program design
The program consists of two threads:

* The main thread reads output from the game.
* The console thread reads what you're typing. Most logic should run on this thread.
