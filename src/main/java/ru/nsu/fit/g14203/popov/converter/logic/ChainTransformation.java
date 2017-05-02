package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.innerview.AndChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.OrChainNode;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AndChain(a, AndChain(b, c, ...), ...)    ->  AndChain(a, b, c, ...)
 * OrChain(a, OrChain(b, c, ...), ...)      ->  OrChain(a, b, c, ...)
 * AndChain(a)                              ->  a
 * OrChain(a)                               ->  a
 *
 * Sorts chain elements in lexicographical order.
 */
public class ChainTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (node instanceof AndChainNode || node instanceof OrChainNode)
            return chain(node);

        return node;
    }

    private Node chain(Node oldChain) {
        boolean[] changed = { false };

        List<Node> children = oldChain.getChildren().stream()
                .flatMap(child -> {
                    if (oldChain.getClass().isInstance(child)) {
                        changed[0] = true;
                        return child.getChildren().stream();
                    }
                    return Stream.of(child);
                })
                .sorted()
                .collect(Collectors.toList());

        if (children.size() == 1)
            return children.get(0);

        Node newChain = (oldChain instanceof AndChainNode) ? new AndChainNode()
                                                           : new OrChainNode();
        newChain.setChildren(children);
        for (Node child : oldChain.getChildren()) {
            child.setParent(newChain);
        }

        if (changed[0])
            return newChain;
        return oldChain;
    }
}
