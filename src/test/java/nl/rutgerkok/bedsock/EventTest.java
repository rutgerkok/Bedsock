package nl.rutgerkok.bedsock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nl.rutgerkok.bedsock.event.Event;
import nl.rutgerkok.bedsock.event.EventHandler;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.event.Listener;
import nl.rutgerkok.bedsock.impl.PrintlnLogger;
import nl.rutgerkok.bedsock.impl.event.EventRegistryImpl;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;
import nl.rutgerkok.bedsock.plugin.Plugin;
import nl.rutgerkok.bedsock.plugin.PluginDescription;

class EventTest {

    public static class MyEvent extends Event {

        boolean successfull = false;

    }

    public static class MyListener implements Listener {

        @EventHandler
        public void onTest(MyEvent event) {
            event.successfull = true;
        }
    }

    private static class MyPlugin implements ActivePlugin {

        @Override
        public Plugin getPlugin() {
            return new Plugin() {};
        }

        @Override
        public PluginDescription getPluginDescription() {
            return () -> "MyPlugin";
        }

        @Override
        public Logger getPluginLogger() {
            return new PrintlnLogger();
        }

    }

    @Test
    public void firing() {
        EventRegistry registry = new EventRegistryImpl();

        registry.registerHandler(new MyPlugin(), new MyListener());

        MyEvent event = new MyEvent();
        assertFalse(event.successfull);
        registry.callEvent(event);
        assertTrue(event.successfull);
    }
}
