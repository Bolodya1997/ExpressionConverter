package ru.nsu.fit.g14203.popov.parse.types;

public class Terminal {

    private TerminalType type;
    private String value;

    public Terminal(TerminalType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
