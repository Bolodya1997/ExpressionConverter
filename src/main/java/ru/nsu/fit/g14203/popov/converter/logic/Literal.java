package ru.nsu.fit.g14203.popov.converter.logic;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.False;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.True;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Literal extends NonTerminalType {

    public Literal() {
        Literal literal = (Literal) BASE_INSTANCES.get(Literal.class);
        if (literal == null)
            return;

        rules = new Type[][]{
                { new True() },
                { new False() }
        };

        literal.rules = rules;
    }
}
