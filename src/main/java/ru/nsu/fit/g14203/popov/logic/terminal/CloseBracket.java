package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class CloseBracket extends TerminalType {

    private final static String CLOSE_BRACKET = ")";

    @Override
    public String getDefaultValue() {
        return CLOSE_BRACKET;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(CLOSE_BRACKET);
    }
}
