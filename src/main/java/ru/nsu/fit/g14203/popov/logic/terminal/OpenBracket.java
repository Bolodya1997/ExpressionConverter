package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class OpenBracket extends TerminalType {

    private final static String OPEN_BRACKET = "(";

    @Override
    public String getDefaultValue() {
        return OPEN_BRACKET;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(OPEN_BRACKET);
    }
}
