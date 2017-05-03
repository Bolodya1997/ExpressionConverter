package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.logic.terminal.And;
import ru.nsu.fit.g14203.popov.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.types.Type;

import java.util.List;

/**
 * a AND b                      ->  AndChain(a, b)
 * a AND AndChain(b, c, ...)    ->  AndChain(a, b, c, ...)
 * a OR b                       ->  OrChain(a, b)
 * a OR OrChain(b, c, ...)      ->  OrChain(a, b, c, ...)
 */
public class ChainSingleTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 3)
            return node;

        Type childType = children.get(1).getType();
        if (childType instanceof And || childType instanceof Or)
            return chain(node, childType);

        return node;
    }

    private Node chain(Node node, Type childType) {
        Node chain = (childType instanceof And) ? new AndChainNode()
                                                : new OrChainNode();

        Node[] children = { node.getChildren().get(0), node.getChildren().get(2) };
        for (Node child : children) {
            if (chain.getClass().isInstance(child))
                chain.getChildren().addAll(child.getChildren());
            else
                chain.getChildren().add(child);
        }
        chain.getChildren().forEach(child -> child.setParent(chain));

        return chain;
    }
}
