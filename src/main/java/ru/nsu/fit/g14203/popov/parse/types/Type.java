package ru.nsu.fit.g14203.popov.parse.types;

import java.util.HashMap;
import java.util.Map;

public class Type {

    private static Class<? extends Type> last = null;

    protected final static Map<Class<? extends Type>, Type> BASE_INSTANCES = new HashMap<>();
    {
put:    if (!BASE_INSTANCES.containsKey(getClass())) {
            if (last == getClass())
                break put;
            last = getClass();

            try {
                BASE_INSTANCES.put(getClass(), getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
