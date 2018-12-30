package nl.rutgerkok.bedsock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.impl.command.CommandArgsImpl;

class CommandArgsTest {

    @Test
    void minSize() throws CommandException {
        ActiveServer server = Mockito.mock(ActiveServer.class);
        CommandArgs args = CommandArgsImpl.parse(server, "my test");

        args.expectMinSize(2); // Must not throw
        assertThrows(CommandException.class, () -> args.expectMinSize(3)); // Must throw
    }

    @Test
    void trim() throws CommandException {
        ActiveServer server = Mockito.mock(ActiveServer.class);
        CommandArgs args = CommandArgsImpl.parse(server, " my  test ");
        assertEquals("my", args.getString(0));
        assertEquals("test", args.getString(1));
        args.expectSize(2);
    }

}