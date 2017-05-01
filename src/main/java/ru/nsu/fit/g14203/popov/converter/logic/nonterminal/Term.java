package ru.nsu.fit.g14203.popov.converter.logic.nonterminal;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.And;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Term extends NonTerminalType {

    public Term() {
        Term term = (Term) BASE_INSTANCES.get(Term.class);
        if (term == null)
            return;

        Type factor = BASE_INSTANCES.get(Factor.class);
        rules = new Type[][]{
                { factor },
                { term, new And(), factor }
        };

        term.rules = rules;
    }
}
