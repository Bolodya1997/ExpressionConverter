package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.And;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.OpenBracket;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.converter.optimisation.Optimisation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * Opens brackets when it is possible without doing any other actions:
 *      ... a AND (b AND c) AND d ...
 *      ... a OR (b AND c) OR d ...
 *      ... a OR (b OR c) OR d ...
 *      ... ((a)) ...
 *      (a)
 */
public class SimpleBracketsOptimisation implements Optimisation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.isEmpty() || !(children.get(0).getType() instanceof OpenBracket))
            return node;

        Node child = children.get(1);
        if (child.getChildren().isEmpty())
            return child;   //  (TRUE), (FALSE), (var)

        if (child.getChildren().size() != 3)
            return node;

        Node parent = node.getParent();
        if (parent == null || parent.getChildren().get(0).getType() instanceof OpenBracket)
            return child;     //  (a), ... ((a)) ...

        List<Node> siblings = parent.getChildren();
        if (siblings.size() != 3)
            return node;

        if (siblings.get(1).getType() instanceof Or)
            return child;

        if (siblings.get(1).getType() instanceof And)
            return and(child);

        return node;
    }

    private Node and(Node child) {
        if (child.getChildren().get(1).getType() instanceof And)
            return child;

        return child.getParent();
    }
}
