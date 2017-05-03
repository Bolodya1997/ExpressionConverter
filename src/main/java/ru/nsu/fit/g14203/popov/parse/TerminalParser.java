package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

import java.util.Arrays;
import java.util.stream.Stream;

final class TerminalParser {

    /**
     * Parses input string into array of terminals of given types.
     *
     * @param types                 acceptable types for output terminals
     * @param string                string to parse
     * @return                      array of terminals
     *
     * @throws ParseException       if string contains fragment which is not correct for any
     *                              of given types
     */
    static Terminal[] parseString(TerminalType[] types, String string) throws ParseException {
        String modified = string;
        for (TerminalType type : types) {
            String toReplace = type.getDefaultValue();
            modified = modified.replace(toReplace, String.format(" %s ", toReplace));
        }

        String[] words = Arrays.stream(modified
                .split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        Stream.Builder<Terminal> terminals = Stream.builder();
        for (String word : words) {
            TerminalType type = Arrays.stream(types)
                    .filter(terminalType -> terminalType.isCorrect(word))
                    .findAny().orElse(null);

            if (type == null) {
                int position = string.indexOf(word);
                throw new ParseException(string, position, ParseException.Reason.PARSE);
            }

            terminals.add(new Terminal(type, word));
        }

        return terminals.build()
                .toArray(Terminal[]::new);
    }
}
