package ru.nsu.fit.g14203.popov.logic;

import ru.nsu.fit.g14203.popov.logic.innerrepresentation.BracketSigleTransformation;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.ChainSingleTransformation;
import ru.nsu.fit.g14203.popov.logic.innerrepresentation.NotSingleTransformation;
import ru.nsu.fit.g14203.popov.logic.nonterminal.*;
import ru.nsu.fit.g14203.popov.logic.transformations.*;
import ru.nsu.fit.g14203.popov.parse.Simplifier;
import ru.nsu.fit.g14203.popov.parse.Transformation;
import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;

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
