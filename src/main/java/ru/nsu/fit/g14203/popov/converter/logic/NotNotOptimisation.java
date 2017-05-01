package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.optimisation.Optimisation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * NOT (NOT a)  ->  a
 */
public class NotNotOptimisation implements Optimisation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 2)
            return node;

        List<Node> grandChildren = children.get(1).getChildren();
        if (grandChildren.size() != 3)
            return node;

        List<Node> grandGrandChildren = grandChildren.get(1).getChildren();
        if (grandGrandChildren.size() != 2)
            return node;

        return grandGrandChildren.get(1);
    }
}
