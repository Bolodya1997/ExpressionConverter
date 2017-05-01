package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.Expression;
import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.Factor;
import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.Primary;
import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.Term;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.*;
import ru.nsu.fit.g14203.popov.converter.optimisation.Optimisation;
import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

import java.util.List;

/**
 * NOT (a AND b)    ->  NOT (a) OR NOT (b)
 * NOT (a OR b)     ->  NOT (a) AND NOT (b)
 */
public class DeMorganOptimisation implements Optimisation {

    @Override
    public Node perform(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() != 2)
            return node;

        List<Node> grandChildren = children.get(1).getChildren();
        if (grandChildren.size() != 3)
            return node;

        List<Node> grandGrandChildren = grandChildren.get(1).getChildren();
        if (grandGrandChildren.size() != 3)
            return node;

        return operator(grandGrandChildren);
    }

    /*
     *              expression
     *      factor    OR/AND    factor
     *     NOT  primary        NOT  primary
     *         (   a   )           (   b   )
     */
    private Node operator(List<Node> grandGrandChildren) {  // TODO: replace "OR", "AND", "NOT", "(", ")"
        Terminal operator;
        if (grandGrandChildren.get(1).getType() instanceof And)
            operator = new Terminal(new Or(), "OR");
        else
            operator = new Terminal(new And(), "AND");

        Terminal not            = new Terminal(new Not(), "NOT");
        Terminal openBracket    = new Terminal(new OpenBracket(), "(");
        Terminal closeBracket   = new Terminal(new CloseBracket(), ")");

        Node expression     = new Node(new Expression());
        Node factor1        = new Node(new Factor());
        Node operatorNode   = new Node(operator);
        Node factor2        = new Node(new Factor());
        Node not1           = new Node(not);
        Node primary1       = new Node(new Primary());
        Node not2           = new Node(not);
        Node primary2       = new Node(new Primary());
        Node openBracket1   = new Node(openBracket);
        Node closeBracket1  = new Node(closeBracket);
        Node openBracket2   = new Node(openBracket);
        Node closeBracket2  = new Node(closeBracket);

        expression.getChildren().add(factor1);
        expression.getChildren().add(operatorNode);
        expression.getChildren().add(factor2);
        expression.getChildren().forEach(child -> child.setParent(expression));

        factor1.getChildren().add(not1);
        factor1.getChildren().add(primary1);
        factor1.getChildren().forEach(child -> child.setParent(factor1));

        primary1.getChildren().add(openBracket1);
        primary1.getChildren().add(grandGrandChildren.get(0));
        primary1.getChildren().add(closeBracket1);
        primary1.getChildren().forEach(child -> child.setParent(primary1));

        factor2.getChildren().add(not2);
        factor2.getChildren().add(primary2);
        factor2.getChildren().forEach(child -> child.setParent(factor2));

        primary2.getChildren().add(openBracket2);
        primary2.getChildren().add(grandGrandChildren.get(2));
        primary2.getChildren().add(closeBracket2);
        primary2.getChildren().forEach(child -> child.setParent(primary2));

        return expression;
    }
}
