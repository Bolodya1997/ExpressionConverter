package ru.nsu.fit.g14203.popov.parse.types;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NonTerminalType extends Type {

    /*
     * Need to fix problem with recursive constructor calls in rules definition:
     *      new NTa() -> new NTb(), new NTc()
     *      new NTb() -> new NTc()
     *      new NTc() -> new NTa()
     *
     * Replace it with:
     *      new NTa() -> BASE_INSTANCES.get(NTb.class), BASE_INSTANCES.get(NTc.class)
     *      new NTb() -> BASE_INSTANCES.get(NTc.class)
     *      new NTc() -> BASE_INSTANCES.get(NTa.class)
     *
     * Now we need to call constructor for every <? extends NonTerminalType> before using it
     * and update rules for BASE_INSTANCES.get(NT.class) in NT constructor, because it would
     * be filled with null references for each <? extends NonTerminalType> which constructor
     * was called first time only after NT constructor first invocation.
     */
    protected final static Map<Class<? extends Type>, Type> BASE_INSTANCES = new HashMap<>();
    {
        if (getClass() != NonTerminalType.class && !BASE_INSTANCES.containsKey(getClass()))
            BASE_INSTANCES.put(getClass(), this);
    }

    /*
     * Chomsky normal form needs to add a lot of temporary non-terminals that must be removed
     * from the parse tree when it was built.
     *
     * For every <? extends NonTerminalType> instance:
     *      instance is not temporary           uuid = NOT_TEMP;
     *      instance is temporary               uuid = UUID.randomUUID();
     */
    private final static UUID NOT_TEMP = new UUID(0, 0);

    protected Type[][] rules;
    private UUID uuid = NOT_TEMP;

    public static NonTerminalType getTemp(Type[][] rules) {
        return new NonTerminalType(rules, UUID.randomUUID());
    }

    protected NonTerminalType() {}

    private NonTerminalType(Type[][] rules, UUID uuid) {
        this.rules = rules;
        this.uuid = uuid;
    }

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
