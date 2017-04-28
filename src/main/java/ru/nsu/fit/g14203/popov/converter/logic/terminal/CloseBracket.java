package ru.nsu.fit.g14203.popov.converter.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class CloseBracket extends TerminalType {

    @Override
    public boolean isInstance(String value) {
        return value.equals(")");
    }
}
