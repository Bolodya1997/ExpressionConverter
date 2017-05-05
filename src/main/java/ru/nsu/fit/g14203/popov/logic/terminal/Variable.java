package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class Variable extends TerminalType {

    @Override
    public String getDefaultValue() {
        return "variable";
    }

    @Override
    public boolean isCorrect(String value) {
        return value.chars().allMatch(Character::isLowerCase);
    }
}
