package ru.nsu.fit.g14203.popov.converter.logic.nonterminal;

import ru.nsu.fit.g14203.popov.converter.logic.terminal.CloseBracket;
import ru.nsu.fit.g14203.popov.converter.logic.terminal.OpenBracket;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

public final class Primary extends NonTerminalType {

    public Primary() {
        Primary primary = (Primary) BASE_INSTANCES.get(Primary.class);
        if (primary == null)
            return;

        Type literal    = BASE_INSTANCES.get(Literal.class);
        Type identifier = BASE_INSTANCES.get(Identifier.class);
        Type expression = BASE_INSTANCES.get(Expression.class);
        rules = new Type[][]{
                { literal },
                { identifier },
                { new OpenBracket(), expression, new CloseBracket() }
        };

        primary.rules = rules;
    }
}
