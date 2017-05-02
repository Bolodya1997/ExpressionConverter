package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.innerview.AndChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.NotNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.OrChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.False;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.True;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AndChain(a, b, c, a, ...)        ->  AndChain(a, b, c, ...)
 * AndChain(a, b, c, Not(a), ...)   ->  FALSE
 * OrChain(a, b, c, a, ...)         ->  OrChain(a, b, c, ...)
 * OrChain(a, b, c, Not(a), ...)    ->  TRUE
 */
public class SameTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (node instanceof AndChainNode || node instanceof OrChainNode)
            return operator(node);

        return node;
    }

    private Node operator(Node node) {
        Terminal onNegative = (node instanceof AndChainNode) ? new Terminal(new False(), "FALSE")
                                                             : new Terminal(new True(), "TRUE");

        List<Node> children = node.getChildren()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (node.getChildren().size() != children.size()) {
            node = (node instanceof AndChainNode) ? new AndChainNode()
                                                  : new OrChainNode();
            node.setChildren(children);
        }

        for (Node child : children) {
            Node tmp = new NotNode();
            tmp.getChildren().add(child);

            Node negative = (child instanceof NotNode) ? child.getChildren().get(0) : tmp;
            if (children.stream()
                    .anyMatch(__child -> __child.equals(negative))) {
                return new Node(onNegative);
            }
        }

        return node;
    }
}
