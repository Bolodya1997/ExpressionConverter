package ru.nsu.fit.g14203.popov.parse.types;

public abstract class TerminalType extends Type {

    public abstract boolean isInstance(String value);
}
