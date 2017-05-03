package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class Or extends TerminalType {

    private final static String OR = "OR";

    @Override
    public String getDefaultValue() {
        return OR;
    }

    @Override
    public boolean isCorrect(String value) {
        return value.equals(OR);
    }
}
