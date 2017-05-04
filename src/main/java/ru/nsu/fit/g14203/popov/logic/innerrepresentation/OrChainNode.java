package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.stream.Collectors;

public class OrChainNode extends Node {

    private final static String OR = new Or().getDefaultValue();

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
    public String toString() {
        return getChildren().stream()
                .map(Node::toString)
                .collect(Collectors.joining(String.format(" %s ", OR)));
    }
}
