package ru.nsu.fit.g14203.popov.logic.transformations;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.AndChainNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.BracketNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.NotNode;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.OrChainNode;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

/**
 * node = Bracket(AndChain(b, c, ...))
 * AndChain(a, Bracket(AndChain(b, c, ...)), ...)   ->  AndChain(a, AndChain(b, c, ...), ...)
 * OrChain(a, Bracket(AndChain(b, c, ...)), ...)    ->  OrChain(a, AndChain(b, c, ...), ...)
 *
 * node = Bracket(OrChain(b, c, ...))
 * OrChain(a, Bracket(OrChain(b, c, ...)), ...)     ->  OrChain(a, OrChain(b, c, ...), ...)
 *
 * node = Bracket(a)
 * ... Bracket(Bracket(a)) ...                      ->  ... Bracket(a) ...
 *
 * ... Bracket(TRUE) ...                            ->  ... TRUE ...
 * ... Bracket(FALSE) ...                           ->  ... FALSE ...
 * ... Bracket(var) ...                             ->  ... var ...
 * Bracket(a)                                       ->  a
 */
public class BracketTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (!(node instanceof BracketNode))
            return node;

        Node child = node.getChildren().get(0);
        if (child instanceof BracketNode || child.getChildren().isEmpty() || node.getParent() == null)
            return child;

        if (child instanceof AndChainNode && !(node.getParent() instanceof NotNode))
            return child;

        if (child instanceof OrChainNode && node.getParent() instanceof OrChainNode)
            return child;

        return node;
    }
}
