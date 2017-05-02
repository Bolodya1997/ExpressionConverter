package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.stream.Collectors;

public class OrChainNode extends Node {

    public OrChainNode() {
        super(new InnerType());
    }

    @Override
    public Node copy() {
        Node result = new OrChainNode();
        getChildren().forEach(child -> result.getChildren().add(child.copy()));

        return result;
    }

    @Override
    public String toString() {  //  TODO: replace "OR"
        return getChildren().stream()
                .map(Node::toString)
                .collect(Collectors.joining(" OR "));
    }
}
