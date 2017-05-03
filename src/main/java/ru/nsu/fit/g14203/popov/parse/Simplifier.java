package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.TerminalType;

import java.util.*;

/**
 * Abstract class for simplifying strings in certain language.
 * Parametrised by:
 *      - list of rules defining grammar
 *      - start non-terminal for derivation
 *      - list of one time transformations for parse tree (can be used to change grammar to equivalent one)
 *      - list of transformations
 */
public abstract class Simplifier {

    /**
     * All classes extends NonTerminalType should be loaded with their constructors invocation
     * before any other usage of any of them.
     *
     * @return                  all NonTerminalType classes used in grammar
     */
    protected abstract Class<? extends NonTerminalType>[] getNonTerminalClasses();

    /**
     * @return                  class of start non-terminal (must be one of all NonTerminal classes)
     */
    protected abstract Class<? extends NonTerminalType> getStartNonTerminalClass();

    private final List<NonTerminalType> nonTerminals = new ArrayList<>();
    private final NonTerminalType startNonTerminal;
    {
        try {

            for (Class<? extends NonTerminalType> __class : getNonTerminalClasses()) {
                __class.newInstance();
            }

            for (Class<? extends NonTerminalType> __class : getNonTerminalClasses()) {
                nonTerminals.add(__class.newInstance());
            }

            startNonTerminal = getStartNonTerminalClass().newInstance();

        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return                  transformations need to be performed one time before all others
     */
    protected abstract Collection<Transformation> getSingleTimeTransformations();

    private final Collection<Transformation> singleTimeTransformations = getSingleTimeTransformations();

    /**
     * @return                  transformations should be performed while tree is changing
     */
    protected abstract Collection<Transformation> getTransformations();

    private final Collection<Transformation> transformations = getTransformations();

    private final Grammar grammar = new Grammar(nonTerminals, startNonTerminal).normalize();
    private final TerminalType[] terminalTypes = grammar.getTerminalTypes();

    public final String simplify(String input) throws ParseException {
        Terminal[] sequence = TerminalParser.parseString(terminalTypes, input);

        ParseTree tree = new ParseTree(grammar, sequence);
        tree = Transformer.transform(tree, singleTimeTransformations);

        ParseTree simplified = Transformer.transform(tree, transformations);
        while (simplified != tree) {
            tree = simplified;
            simplified = Transformer.transform(tree, transformations);
        }

        return tree.toString();
    }
}
