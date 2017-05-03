package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class False extends TerminalType {

    private final static String FALSE = "FALSE";

    @Override
    public String getDefaultValue() {
        return FALSE;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(FALSE);
    }
}
