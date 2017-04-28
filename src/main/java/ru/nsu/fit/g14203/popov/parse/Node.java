package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {

    private Type type;
    private Terminal terminal;

    private List<Node> children = new ArrayList<>();

    public Node(Type type) {
        this.type = type;
    }

    public Node(Terminal terminal) {
        type = terminal.getType();
        this.terminal = terminal;
    }

    public Type getType() {
        return type;
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        if (terminal != null)
            return terminal.getValue();

        return children.stream()
                .map(Object::toString)
                .collect(Collectors.joining("  "));
    }
}
