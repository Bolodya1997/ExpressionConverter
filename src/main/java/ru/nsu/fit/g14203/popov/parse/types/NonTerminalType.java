package ru.nsu.fit.g14203.popov.parse.types;

import java.util.UUID;

public class NonTerminalType extends Type {

    private final static UUID NOT_TEMP = new UUID(0, 0);

    protected Type[][] rules;
    private UUID uuid = NOT_TEMP;

    public static NonTerminalType getTemp(Type[][] rules) {
        return new NonTerminalType(rules, UUID.randomUUID());
    }

    private NonTerminalType(Type[][] rules, UUID uuid) {
        this.rules = rules;
        this.uuid = uuid;
    }

    protected NonTerminalType() {}

    public Type[][] getRules() {
        return rules;
    }

    public void setRules(Type[][] rules) {
        if (BASE_INSTANCES.get(getClass()) == this)
            throw new RuntimeException("Base class rules override");

        this.rules = rules;
    }

    public NonTerminalType copy() {
        NonTerminalType instance;
        try {
            instance = getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        instance.rules = rules;
        instance.uuid = uuid;

        return instance;
    }

    public boolean isTemp() {
        return !uuid.equals(NOT_TEMP);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        NonTerminalType nonTerminalType = (NonTerminalType) obj;
        return uuid.equals(nonTerminalType.uuid);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ uuid.hashCode();
    }

    @Override
    public String toString() {
        if (isTemp())
            return uuid.toString().split("-")[0];

        return super.toString();
    }
}
