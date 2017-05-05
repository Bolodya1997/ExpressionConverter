package ru.nsu.fit.g14203.popov.parse.types;

public class Terminal {

    private final TerminalType type;
    private final String value;

    public Terminal(TerminalType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return value;
    }
}
