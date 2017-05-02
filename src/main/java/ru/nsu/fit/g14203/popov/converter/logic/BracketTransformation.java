package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.innerview.AndChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.BracketNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.NotNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.OrChainNode;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
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
