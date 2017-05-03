package ru.nsu.fit.g14203.popov.logic.terminal;

import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

public final class Literal extends TerminalType {

    @Override
    public String getDefaultValue() {
        return "literal";
    }

    @Override
    public boolean isCorrect(String value) {
        return value.chars().allMatch(Character::isLowerCase);
    }
}
