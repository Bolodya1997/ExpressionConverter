package ru.nsu.fit.g14203.popov.logic.transformations;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.AndChainNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.OrChainNode;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * OrChain(AndChain(a, b, c, ...), AndChain(a, c, ...), ...)    ->  OrChain(AndChain(a, c, ...), ...)
 * AndChain(OrChain(a, b, c, ...), OrChain(a, c, ...), ...)     ->  AndChain(OrChain(a, c, ...), ...)
 */
public class AbsorptionTransformation implements Transformation {

    private final static Transformation CHAIN_TRANSFORMATION    = new ChainTransformation();
    private final static Transformation SAME_TRANSFORMATION     = new SameTransformation();

    @Override
    public Node perform(Node node) {
        if (!(node instanceof AndChainNode || node instanceof OrChainNode))
            return node;

        Class childrenClass = (node instanceof OrChainNode) ? AndChainNode.class
                                                            : OrChainNode.class;
        boolean changed = false;

        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size() - 1 && !changed; i++) {
            for (int k = i + 1; k < children.size(); k++) {
                if (childrenClass.isInstance(children.get(i)) && childrenClass.isInstance(children.get(k)))
                    changed = absorb(children, i, k);
            }
        }

        if (changed)
            return node.copy();
        return node;
    }

    private boolean absorb(List<Node> children, int i, int k) {
        Node nodeParent = children.get(i).copy();
        Node nodeChild = children.get(k).copy();

        nodeParent.getChildren().add(nodeChild);
        nodeChild.setParent(nodeParent);

        nodeParent = CHAIN_TRANSFORMATION.perform(nodeParent);
        nodeParent = SAME_TRANSFORMATION.perform(nodeParent);

        if (children.get(i).equals(nodeParent)) {
            children.remove(i);
            return true;
        }

        if (children.get(k).equals(nodeParent)) {
            children.remove(k);
            return true;
        }

        return false;
    }
}
