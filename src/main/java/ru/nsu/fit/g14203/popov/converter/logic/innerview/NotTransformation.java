package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.Not;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * NOT a    ->  Not(a)
 */
class NotTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 2 || !(children.get(0).getType() instanceof Not))
            return node;

        Node not = new NotNode();
        not.getChildren().add(children.get(1));
        children.get(1).setParent(not);

        return not;
    }
}
