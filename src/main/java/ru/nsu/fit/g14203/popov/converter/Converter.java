package ru.nsu.fit.g14203.popov.converter;

import ru.nsu.fit.g14203.popov.converter.logic.*;
import ru.nsu.fit.g14203.popov.converter.logic.innerview.InnerViewTransformer;
import ru.nsu.fit.g14203.popov.converter.logic.nonterminal.*;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformation;
import ru.nsu.fit.g14203.popov.converter.optimisation.Transformer;
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

    private List<Transformation> transformations = new ArrayList<>();
    {
        transformations.add(new TrueFalseTransformation());
        transformations.add(new BracketTransformation());
        transformations.add(new DeMorganTransformation());
        transformations.add(new NotNotTransformation());
        transformations.add(new ChainTransformation());
        transformations.add(new SameTransformation());
        transformations.add(new OrAndBracketTransformation());
    }

    public Converter(Reader input, Writer output) throws IOException {
        Scanner scanner = new Scanner(input);

        Grammar grammar = new Grammar(nonTerminalTypes, new Expression());

        TerminalParser parser = new TerminalParser(grammar.getTerminals());
        Terminal[] word = parser.parseString(scanner.nextLine());

        ParseTree tree = new ParseTree(grammar, word);

        InnerViewTransformer innerViewTransformer = new InnerViewTransformer();
        tree = innerViewTransformer.transform(tree);

        Transformer transformer = new Transformer(transformations);

        ParseTree transformed = transformer.transform(tree);
        while (transformed != tree) {
            tree = transformed;
            transformed = transformer.transform(tree);
        }

        tree.print();
    }
}
