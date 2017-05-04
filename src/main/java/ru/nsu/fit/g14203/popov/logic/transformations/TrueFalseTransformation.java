package ru.nsu.fit.g14203.popov.logic.transformations;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.AndChainNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.NotNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.OrChainNode;
import ru.nsu.fit.g14203.popov.logic.terminal.*;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Not(TRUE)                    ->  FALSE
 * Not(FALSE)                   ->  TRUE
 * AndChain(a, b, TRUE, ...)    ->  AndChain(a, b, ...)
 * AndChain(a, b, FALSE, ...)   ->  FALSE
 * OrChain(a, b, TRUE, ...)     ->  TRUE
 * OrChain(a, b, FALSE, ...)    ->  OrChain(a, b, ...)
 */
public class TrueFalseTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (node instanceof NotNode)
            return not(node);

        if (node instanceof AndChainNode)
            return and(node);

        if (node instanceof OrChainNode)
            return or(node);

        return node;
    }

    private Node not(Node node) {
        if (node.getChildren().get(0).getType() instanceof True) {
            False __false = new False();
            return new Node(new Terminal(__false, __false.getDefaultValue()));
        }

        if (node.getChildren().get(0).getType() instanceof False) {
            True __true = new True();
            return new Node(new Terminal(__true, __true.getDefaultValue()));
        }

        return node;
    }

    private Node and(Node node) {
        List<Node> children = node.getChildren();

        Node __false = children.stream()
                .filter(child -> child.getType() instanceof False)
                .findAny().orElse(null);
        if (__false != null)
            return __false;

        children = children.stream()
                .filter(child -> !(child.getType() instanceof True))
                .collect(Collectors.toList());
        if (children.isEmpty()) {
            True __true = new True();
            return new Node(new Terminal(__true, __true.getDefaultValue()));
        }

        if (node.getChildren().size() == children.size())
            return node;

        node = new AndChainNode();
        node.setChildren(children);
        for (Node child : children) {
            child.setParent(node);
        }

        return node;
    }

    private Node or(Node node) {
        List<Node> children = node.getChildren();

        Node __true = children.stream()
                .filter(child -> child.getType() instanceof True)
                .findAny().orElse(null);
        if (__true != null)
            return __true;

        children = children.stream()
                .filter(child -> !(child.getType() instanceof False))
                .collect(Collectors.toList());
        if (children.isEmpty()) {
            False __false = new False();
            return new Node(new Terminal(__false, __false.getDefaultValue()));
        }

        if (node.getChildren().size() == children.size())
            return node;

        node = new OrChainNode();
        node.setChildren(children);
        for (Node child : children) {
            child.setParent(node);
        }

        return node;
    }
}
