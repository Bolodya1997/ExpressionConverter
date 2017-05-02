package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node implements Comparable<Node> {

    private Type type;
    private Terminal terminal;

    private Node parent;
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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node copy() {
        if (terminal != null)
            return new Node(terminal);

        Node result = new Node(type);
        children.forEach(child -> result.children.add(child.copy()));

        return result;
    }

    @Override
    public String toString() {
        if (terminal != null)
            return terminal.getValue();

        return children.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Node && toString().equals(obj.toString());

    }

    @Override
    public int compareTo(Node o) {
        return toString().compareTo(o.toString());
    }
}
