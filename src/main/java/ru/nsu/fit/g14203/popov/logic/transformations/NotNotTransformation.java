package ru.nsu.fit.g14203.popov.logic.transformations;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.BracketNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.NotNode;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

/**
 * Not(Bracket(Not(a)))  ->  a
 */
public class NotNotTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (!(node instanceof NotNode))
            return node;

        Node child = node.getChildren().get(0);
        if (!(child instanceof BracketNode))
            return node;

        Node grandChild = child.getChildren().get(0);
        if (!(grandChild instanceof NotNode))
            return node;

        return grandChild.getChildren().get(0);
    }
}
