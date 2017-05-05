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
 *      - list of one time transformations for parse tree
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
     * @return                  class of start non-terminal (must be one of NonTerminal classes)
     */
    protected abstract Class<? extends NonTerminalType> getStartNonTerminalClass();

    private final List<NonTerminalType> nonTerminals = new ArrayList<>();
    private final NonTerminalType startNonTerminal;
    {
        Class<? extends NonTerminalType>[] nonTerminalClasses = getNonTerminalClasses();
        try {

            for (Class<? extends NonTerminalType> __class : nonTerminalClasses) {
                __class.newInstance();
            }

            for (Class<? extends NonTerminalType> __class : nonTerminalClasses) {
                nonTerminals.add(__class.newInstance());
            }

            startNonTerminal = getStartNonTerminalClass().newInstance();

        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invokes when the input string cannot be transformed into the parse tree.
     *
     * @param input             input string
     * @param sequence          terminal sequence of input string
     * @param position          position of first error
     * @return                  exception to rise
     */
    protected abstract ParseException reportParseTreeError(String input, Terminal[] sequence,
                                                           int position);

    /**
     * @return                  transformations need to be performed one time before all others
     */
    protected abstract Collection<Transformation> getSingleTimeTransformations();

    private final Collection<Transformation> singleTimeTransformations =
            getSingleTimeTransformations();

    /**
     * @return                  transformations should be performed while tree is changing
     */
    protected abstract Collection<Transformation> getTransformations();

    private final Collection<Transformation> transformations = getTransformations();

    private final Grammar grammar = new Grammar(nonTerminals, startNonTerminal).normalize();
    private final TerminalType[] terminalTypes = grammar.getTerminalTypes();

    public final String simplify(String input) throws ParseException {
        Terminal[] sequence = TerminalParser.parseString(terminalTypes, input);

        ParseTree tree;
        try {
            tree = new ParseTree(grammar, sequence);
        } catch (ParseTree.TreeException e) {
            throw reportParseTreeError(input, sequence, e.position);
        }
        tree = Transformer.transform(tree, singleTimeTransformations);

        ParseTree simplified = Transformer.transform(tree, transformations);
        while (simplified != tree) {
            tree = simplified;
            simplified = Transformer.transform(tree, transformations);
        }

        return tree.toString();
    }
}
