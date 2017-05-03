package ru.nsu.fit.g14203.popov.parse.types;

public class Type {

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
