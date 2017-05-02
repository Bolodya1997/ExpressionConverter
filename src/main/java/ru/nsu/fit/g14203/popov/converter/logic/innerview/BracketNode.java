package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.parse.Node;

public class BracketNode extends Node {

    public BracketNode() {
        super(new InnerType());
    }

    @Override
    public Node copy() {
        Node result = new BracketNode();
        getChildren().forEach(child -> result.getChildren().add(child.copy()));

        return result;
    }

    @Override
    public String toString() {  //  TODO: replace "(", ")"
        return String.format("(%s)", super.toString());
    }
}
