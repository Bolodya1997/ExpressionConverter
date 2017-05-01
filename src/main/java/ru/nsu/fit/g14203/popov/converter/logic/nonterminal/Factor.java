package ru.nsu.fit.g14203.popov.converter.logic.nonterminal;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.Not;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Factor extends NonTerminalType {

    public Factor() {
        Factor factor = (Factor) BASE_INSTANCES.get(Factor.class);
        if (factor == null)
            return;

        Type primary = BASE_INSTANCES.get(Primary.class);
        rules = new Type[][]{
                { new Not(), primary },
                { primary }
        };

        factor.rules = rules;
    }
}
