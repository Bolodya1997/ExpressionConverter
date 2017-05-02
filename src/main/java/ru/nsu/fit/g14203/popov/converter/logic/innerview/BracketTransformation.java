package ru.nsu.fit.g14203.popov.converter.logic.innerview;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.OpenBracket;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

import java.util.List;

/**
 * (a)  ->  Bracket(a)
 */
class BracketTransformation implements Transformation {

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
