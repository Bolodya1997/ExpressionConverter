package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.stream.Collectors;

public class AndChainNode extends Node {

    public AndChainNode() {
        super(new InnerType());
    }

    @Override
    public Node copy() {
        Node result = new AndChainNode();
        getChildren().forEach(child -> result.getChildren().add(child.copy()));

        return result;
    }

    @Override
    public String toString() {  //  TODO: replace "AND"
        return getChildren().stream()
                .map(Node::toString)
                .collect(Collectors.joining(" AND "));
    }
}
