package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.logic.terminal.Not;
import ru.nsu.fit.g14203.popov.parse.Node;

public class NotNode extends Node {

    private final static String NOT = new Not().getDefaultValue();

    public NotNode() {
        super(new InnerType());
    }

    @Override
    public Node copy() {
        Node result = new NotNode();
        getChildren().forEach(child -> result.getChildren().add(child.copy()));

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s", NOT, super.toString());
    }
}
