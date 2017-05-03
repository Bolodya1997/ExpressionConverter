package ru.nsu.fit.g14203.popov.logic.nonterminal;

import ru.nsu.fit.g14203.popov.logic.terminal.Or;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Expression extends NonTerminalType {

    public Expression() {
        Expression expression = (Expression) BASE_INSTANCES.get(Expression.class);
        if (expression == null)
            return;

        Type term = BASE_INSTANCES.get(Term.class);
        rules = new Type[][]{
                { term },
                { expression, new Or(), term }
        };

        expression.rules = rules;
    }
}
