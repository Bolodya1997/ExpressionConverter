package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class True extends TerminalType {

    private final static String TRUE = "TRUE";

    @Override
    public String getDefaultValue() {
        return TRUE;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(TRUE);
    }
}
