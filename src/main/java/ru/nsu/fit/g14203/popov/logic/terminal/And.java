package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class And extends TerminalType {

    private final static String AND = "AND";

    @Override
    public String getDefaultValue() {
        return AND;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(AND);
    }
}
