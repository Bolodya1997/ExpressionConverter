package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.logic.terminal.OpenBracket;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * (a)  ->  Bracket(a)
 */
public class BracketSigleTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 3 || !(children.get(0).getType() instanceof OpenBracket))
            return node;

        Node bracket = new BracketNode();
        bracket.getChildren().add(children.get(1));
        children.get(1).setParent(bracket);

        return bracket;
    }
}
