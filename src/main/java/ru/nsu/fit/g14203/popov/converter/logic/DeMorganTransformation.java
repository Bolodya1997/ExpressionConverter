package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.innerview.AndChainNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.BracketNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.NotNode;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.OrChainNode;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.parse.Node;

/**
 * Not(Bracket(AndChain(a, b, ...)))    ->  Bracket(OrChain(Not(Bracket(a)), Not(Bracket(b)), ...))
 * Not(Bracket(OrChain(a, b, ...)))     ->  Bracket(AndChain(Not(Bracket(a)), Not(Bracket(b)), ...))
 */
public class DeMorganTransformation implements Transformation {

    @Override
    public Node perform(Node node) {
        if (!(node instanceof NotNode))
            return node;

        Node child = node.getChildren().get(0);
        if (!(child instanceof BracketNode))
            return node;

        Node grandChild = child.getChildren().get(0);
        if (grandChild instanceof AndChainNode || grandChild instanceof OrChainNode)
            return chain(grandChild);

        return node;
    }

    private Node chain(Node oldChain) {
        Node newChain = (oldChain instanceof AndChainNode) ? new OrChainNode()
                                                           : new AndChainNode();

        oldChain.getChildren().forEach(child -> {
            Node not = new NotNode();
            newChain.getChildren().add(not);
            not.setParent(newChain);

            Node inBracket = new BracketNode();
            not.getChildren().add(inBracket);
            inBracket.setParent(not);

            inBracket.getChildren().add(child);
            child.setParent(inBracket);
        });

        Node outBracket = new BracketNode();
        outBracket.getChildren().add(newChain);
        newChain.setParent(outBracket);

        return outBracket;
    }
}
