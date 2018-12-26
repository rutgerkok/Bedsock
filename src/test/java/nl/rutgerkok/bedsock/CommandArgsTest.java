package nl.rutgerkok.bedsock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandException;

class CommandArgsTest {

    @Test
    void minSize() throws CommandException {
        CommandArgs args = new CommandArgs("my test");

        args.expectMinSize(2); // Must not throw
        assertThrows(CommandException.class, () -> args.expectMinSize(3)); // Must throw
    }

    @Test
    void trim() throws CommandException {
        CommandArgs args = new CommandArgs(" my  test ");
        assertEquals("my", args.getString(0));
        assertEquals("test", args.getString(1));
        args.expectSize(2);
    }

}