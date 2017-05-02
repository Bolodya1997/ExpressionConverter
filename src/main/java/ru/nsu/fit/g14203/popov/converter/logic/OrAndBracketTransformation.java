package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.innerview.AndChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.BracketNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.OrChainNode;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * AndChain(a, Bracket(OrChain(b, c, ...)), d, ...)  ->
 *      OrChain(AndChain(a, b, d, ...), AndChain(a, c, d, ...), ...AndChain)
 */
public class OrAndBracketTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (!(node instanceof AndChainNode))
            return node;

        Node orChild = null;

        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);

            if (child instanceof BracketNode && child.getChildren().get(0) instanceof OrChainNode) {
                orChild = child.getChildren().get(0);
                children.remove(i);
                break;
            }
        }

        if (orChild == null)
            return node;

        Node orChain = new OrChainNode();
        orChild.getChildren().forEach(grandChild -> {
            Node andChain = node.copy();
            orChain.getChildren().add(andChain);
            andChain.setParent(orChain);

            andChain.getChildren().add(grandChild);
            grandChild.setParent(andChain);
        });

        return orChain;
    }
}
