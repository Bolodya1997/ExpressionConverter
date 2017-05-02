package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.And;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * a AND b                      ->  AndChain(a, b)
 * a AND AndChain(b, c, ...)    ->  AndChain(a, b, c, ...)
 * a OR b                       ->  OrChain(a, b)
 * a OR OrChain(b, c, ...)      ->  OrChain(a, b, c, ...)
 */
class ChainTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 3)
            return node;

        if (children.get(1).getType() instanceof And)
            return and(node);

        if (children.get(1).getType() instanceof Or)
            return or(node);

        return node;
    }

    private Node and(Node node) {
        Node andChain = new AndChainNode();

        Node[] children = { node.getChildren().get(0), node.getChildren().get(2) };
        for (Node child : children) {
            if (child instanceof AndChainNode)
                andChain.getChildren().addAll(child.getChildren());
            else
                andChain.getChildren().add(child);
        }
        andChain.getChildren().forEach(child -> child.setParent(andChain));

        return andChain;
    }

    private Node or(Node node) {
        Node orChain = new OrChainNode();

        Node[] children = { node.getChildren().get(0), node.getChildren().get(2) };
        for (Node child : children) {
            if (child instanceof OrChainNode)
                orChain.getChildren().addAll(child.getChildren());
            else
                orChain.getChildren().add(child);
        }
        orChain.getChildren().forEach(child -> child.setParent(orChain));

        return orChain;
    }
}
