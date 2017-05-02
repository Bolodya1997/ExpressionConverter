package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.parse.Node;

public class NotNode extends Node {

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
    public String toString() {  //  TODO: replace "NOT"
        return String.format("NOT %s", super.toString());
    }
}
