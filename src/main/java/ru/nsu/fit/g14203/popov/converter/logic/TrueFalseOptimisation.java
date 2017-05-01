package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.And;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.False;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.True;
import ru.nsu.fit.g14203.popov.converter.optimisation.Optimisation;
import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

public class TrueFalseOptimisation implements Optimisation {

    @Override
    public Node perform(Node node) {
        if (node.getChildren().size() == 2)
            return not(node);

        if (node.getChildren().size() == 3) {
            if (node.getChildren().get(1).getType() instanceof Or)
                return or(node);

            if (node.getChildren().get(1).getType() instanceof And)
                return and(node);
        }

        return node;
    }

    private Node not(Node node) {   //  TODO: replace "TRUE", "FALSE"
        if (node.getChildren().get(1).getType() instanceof True) {
            Terminal t = new Terminal(new False(), "FALSE");
            return new Node(t);
        }

        if (node.getChildren().get(1).getType() instanceof False) {
            Terminal t = new Terminal(new True(), "TRUE");
            return new Node(t);
        }

        return node;
    }

    private Node or(Node node) {
        Node left = node.getChildren().get(0);
        Node right = node.getChildren().get(2);

        if (left.getType() instanceof True)
            return left;

        if (left.getType() instanceof False)
            return right;

        if (right.getType() instanceof True)
            return right;

        if (right.getType() instanceof False)
            return left;

        return node;
    }

    private Node and(Node node) {
        Node left = node.getChildren().get(0);
        Node right = node.getChildren().get(2);

        if (left.getType() instanceof True)
            return right;

        if (left.getType() instanceof False)
            return left;

        if (right.getType() instanceof True)
            return left;

        if (right.getType() instanceof False)
            return right;

        return node;
    }
}
