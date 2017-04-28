package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TerminalParser {

    private TerminalType[] types;

    public TerminalParser(TerminalType[] types) {
        this.types = types;
    }

    public Terminal[] parseString(String string) throws ParseException {
        String[] words = Arrays.stream(string
                .replace("(", "( ")
                .replace(")", ") ")
                .split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        Stream.Builder<Terminal> terminals = Stream.builder();
        for (String word : words) {
            TerminalType type = Arrays.stream(types)
                    .filter(terminalType -> terminalType.isInstance(word))
                    .findAny().orElse(null);

            if (type == null)
                throw new ParseException(String.format("Cannot parse input fragment: %s\n", word));

            terminals.add(new Terminal(type, word));
        }

        return terminals.build()
                .toArray(Terminal[]::new);
    }
}
