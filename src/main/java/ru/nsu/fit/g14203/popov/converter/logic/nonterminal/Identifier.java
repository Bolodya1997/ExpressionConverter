package ru.nsu.fit.g14203.popov.converter.logic.nonterminal;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.Variable;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Identifier extends NonTerminalType {

    public Identifier() {
        Identifier identifier = (Identifier) BASE_INSTANCES.get(Identifier.class);
        if (identifier == null)
            return;

        rules = new Type[][]{
                { new Variable() }
        };

        identifier.rules = rules;
    }
}
