package ru.nsu.fit.g14203.popov.parse;

import java.io.IOException;

public class ParseException extends IOException {

    enum Reason {
        PARSE,
        TREE
    }

    private static String createMessage(String string, int position, Reason reason) {
        String cause = "";
        switch (reason) {
            case PARSE:
                cause = "Cannot parse input fragment";
                break;
            case TREE:
                cause = "Unexpected token";
        }

        String message = String.format("Parse error at %d: %s", position, cause);
        String point = pointAt(position);

        return String.format("%s\n%s\n%s", message, string, point);
    }

    private static String pointAt(int position) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < position; i++) {
            builder.append(' ');
        }
        builder.append('^');

        return builder.toString();
    }

    ParseException(String string, int position, Reason reason) {
        super(createMessage(string, position, reason));
    }
}
