package ru.nsu.fit.g14203.popov.logic;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.BracketSigleTransformation;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.ChainSingleTransformation;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.NotSingleTransformation;
import ru.nsu.fit.g14203.popov.logic.nonterminal.*;
import ru.nsu.fit.g14203.popov.logic.terminal.*;
import ru.nsu.fit.g14203.popov.logic.transformations.*;
import ru.nsu.fit.g14203.popov.parse.ParseException;
import ru.nsu.fit.g14203.popov.parse.Simplifier;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogicSimplifier extends Simplifier {

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends NonTerminalType>[] getNonTerminalClasses() {
        return new Class[]{
                Expression.class,
                Factor.class,
                Identifier.class,
                Literal.class,
                Primary.class,
                Term.class
        };
    }

    @Override
    protected Class<? extends NonTerminalType> getStartNonTerminalClass() {
        return Expression.class;
    }

    private final static String UNEXPECTED_END      = "Unexpected end of input";
    private final static String UNEXPECTED_TOKEN    = "Unexpected token";

    @Override
    protected ParseException reportParseTreeError(String input, Terminal[] sequence,
                                                  int position) {
        if (position == -1) {
            return new ParseException("", 0, 0, "Empty string");
        }

        /*
         * CYK algorithm is not any good in finding correct error position and I can get only
         * something from (position - 1, position), so this method would be fat and ugly.
         */

        String reasonType;
        int pos;
        int len;
        String reasonText;

        Terminal cur = sequence[position];
        if ((position == 0 || sequence[position - 1].getType() instanceof OpenBracket) &&
                (cur.getType() instanceof And || cur.getType() instanceof Or ||
                        cur.getType() instanceof CloseBracket)) {
            reasonType = UNEXPECTED_TOKEN;
            pos = computePos(input, sequence, position);
            len = cur.toString().length();

            reasonText = startClause(cur);

        } else if (cur.getType() instanceof OpenBracket) {
            reasonType = UNEXPECTED_END;
            pos = input.length();
            len = 1;

            reasonText = "wanted: CLOSE_BRACKET";

        } else if (cur.getType() instanceof CloseBracket) {
            reasonType = UNEXPECTED_TOKEN;
            pos = computePos(input, sequence, position);
            len = sequence[position].toString().length();

            if (sequence[position - 1].getType() instanceof OpenBracket)
                reasonText = startClause(cur);
            else
                reasonText = "got: CLOSE_BRACKET without OPEN_BRACKET";

        } else if (cur.getType() instanceof And || cur.getType() instanceof Or) {
            Terminal prev = sequence[position - 1];
            if (prev.getType() instanceof Not ||
                    prev.getType() instanceof And || prev.getType() instanceof Or) {
                reasonType = UNEXPECTED_TOKEN;
                pos = computePos(input, sequence, position);
                len = sequence[position].toString().length();

                if (sequence[position - 1].getType() instanceof Not)
                    reasonText = notClause(cur);
                else
                    reasonText = andOrClause(cur);

            } else {
                reasonType = UNEXPECTED_END;
                pos = input.length();
                len = 1;

                reasonText = andOrClause(null);
            }

        } else if (cur.getType() instanceof Not) {
            if (position == 0) {
                reasonType = UNEXPECTED_END;
                pos = input.length();
                len = 1;

                reasonText = notClause(null);

            } else {
                Terminal prev = sequence[position - 1];
                if (prev.getType() instanceof CloseBracket || prev.getType() instanceof Variable ||
                        prev.getType() instanceof True || prev.getType() instanceof False) {
                    reasonType = UNEXPECTED_TOKEN;
                    pos = computePos(input, sequence, position);
                    len = cur.toString().length();

                    reasonText = lastClause(cur);

                } else if (prev.getType() instanceof Not) {
                    reasonType = UNEXPECTED_TOKEN;
                    pos = computePos(input, sequence, position);
                    len = cur.toString().length();

                    reasonText = notClause(cur);

                } else {
                    reasonType = UNEXPECTED_END;
                    pos = input.length();
                    len = 1;

                    reasonText = notClause(null);
                }
            }
        } else {
            reasonType = UNEXPECTED_TOKEN;
            if (position == 0) {
                pos = computePos(input, sequence, position + 1);
                len = sequence[position + 1].toString().length();

                reasonText = lastClause(sequence[position + 1]);

            } else {
                Terminal prev = sequence[position - 1];
                if (prev.getType() instanceof CloseBracket || prev.getType() instanceof Variable ||
                        prev.getType() instanceof True || prev.getType() instanceof False) {
                    pos = computePos(input, sequence, position);
                    len = cur.toString().length();

                    reasonText = lastClause(cur);

                } else {
                    pos = computePos(input, sequence, position + 1);
                    len = sequence[position + 1].toString().length();

                    reasonText = lastClause(sequence[position + 1]);
                }
            }
        }

        String reason = String.format("%s (%s)", reasonType, reasonText);
        return new ParseException(input, pos, len, reason);
    }

    private static int computePos(String input, Terminal[] sequence, int position) {
        int pos = 0;
        for (int i = 0; i < position; i++) {
            pos = input.indexOf(sequence[i].toString(), pos);
            pos += sequence[i].toString().length();
        }

        return input.indexOf(sequence[position].toString(), pos);
    }

    private static String startClause(Terminal terminal) {
        String got = terminal.toString();
        if (terminal.getType() instanceof CloseBracket)
            got = "CLOSE_BRACKET";

        return String.format("wanted: NOT, LITERAL, IDENTIFIER, OPEN_BRACKET; got: %s", got);
    }

    private static String andOrClause(Terminal terminal) {
        String wanted = "wanted: LITERAL, IDENTIFIER, NOT";
        if (terminal == null)
            return wanted;

        String got = terminal.toString();
        if (terminal.getType() instanceof OpenBracket)
            got = "OPEN_BRACKET";
        if (terminal.getType() instanceof CloseBracket)
            got = "CLOSE_BRACKET";

        return String.format("%s; got: %s", wanted, got);
    }

    private static String notClause(Terminal terminal) {
        String wanted = "wanted: LITERAL, IDENTIFIER, OPEN_BRACKET";
        if (terminal == null)
            return wanted;

        String got = terminal.toString();
        if (terminal.getType() instanceof CloseBracket)
            got = "CLOSE_BRACKET";

        return String.format("%s; got: %s", wanted, got);
    }

    private static String lastClause(Terminal terminal) { //  Variable, True, False, CloseBracket
        String got = terminal.toString();
        if (terminal.getType() instanceof OpenBracket)
            got = "OPEN_BRACKET";
        if (terminal.getType() instanceof Variable)
            got = "IDENTIFIER";
        if (terminal.getType() instanceof True || terminal.getType() instanceof False)
            got = "LITERAL";

        return String.format("wanted: AND, OR; got: %s", got);
    }

    @Override
    protected Collection<Transformation> getSingleTimeTransformations() {
        return Stream.<Transformation>builder()
                .add(new BracketSigleTransformation())
                .add(new ChainSingleTransformation())
                .add(new NotSingleTransformation())
                .build()
                .collect(Collectors.toList());
    }

    @Override
    protected Collection<Transformation> getTransformations() {
        return Stream.<Transformation>builder()
                .add(new AbsorptionTransformation())
                .add(new BracketTransformation())
                .add(new ChainTransformation())
                .add(new DeMorganTransformation())
                .add(new DistributeTransformation())
                .add(new NotNotTransformation())
                .add(new SameTransformation())
                .add(new TrueFalseTransformation())
                .build()
                .collect(Collectors.toList());
    }
}
