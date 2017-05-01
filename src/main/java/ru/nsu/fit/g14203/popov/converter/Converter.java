package ru.nsu.fit.g14203.popov.converter;

import ru.nsu.fit.g14203.popov.converter.logic.DeMorganOptimisation;
import ru.nsu.fit.g14203.popov.converter.logic.NotNotOptimisation;
import ru.nsu.fit.g14203.popov.converter.logic.SimpleBracketsOptimisation;
import ru.nsu.fit.g14203.popov.converter.logic.TrueFalseOptimisation;
import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.*;
import ru.nsu.fit.g14203.popov.converter.optimisation.Optimisation;
import ru.nsu.fit.g14203.popov.converter.optimisation.Optimiser;
import ru.nsu.fit.g14203.popov.parse.Grammar;
import ru.nsu.fit.g14203.popov.parse.ParseTree;
import ru.nsu.fit.g14203.popov.parse.TerminalParser;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Converter {

    static  {   //  load classes
        new Identifier();
        new Literal();
        new Expression();
        new Term();
        new Factor();
        new Primary();
    }

    private List<NonTerminalType> nonTerminalTypes = new ArrayList<>();
    {
        nonTerminalTypes.add(new Identifier());
        nonTerminalTypes.add(new Literal());
        nonTerminalTypes.add(new Expression());
        nonTerminalTypes.add(new Term());
        nonTerminalTypes.add(new Factor());
        nonTerminalTypes.add(new Primary());
    }

    private List<Optimisation> optimisations = new ArrayList<>();
    {
        optimisations.add(new TrueFalseOptimisation());
        optimisations.add(new SimpleBracketsOptimisation());
        optimisations.add(new DeMorganOptimisation());
        optimisations.add(new NotNotOptimisation());
    }

    public Converter(Reader input, Writer output) throws IOException {
        Scanner scanner = new Scanner(input);

        Grammar grammar = new Grammar(nonTerminalTypes, new Expression());

        TerminalParser parser = new TerminalParser(grammar.getTerminals());
        Terminal[] word = parser.parseString(scanner.nextLine());

        ParseTree tree = new ParseTree(grammar, word);

        Optimiser optimiser = new Optimiser(optimisations);
        optimiser.addOptimisation(new TrueFalseOptimisation());

        ParseTree optimised = optimiser.optimise(tree);
        while (optimised != tree) {
            tree = optimised;
            optimised = optimiser.optimise(tree);
        }

        optimised.print();
    }
}
