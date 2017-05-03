package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class Not extends TerminalType {

    private final static String NOT = "NOT";

    @Override
    public String getDefaultValue() {
        return NOT;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(NOT);
    }
}
