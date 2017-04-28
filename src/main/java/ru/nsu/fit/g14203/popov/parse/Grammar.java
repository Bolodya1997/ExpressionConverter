package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.TerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Type;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {

    private Comparator<NonTerminalType> startComparator;

    private TerminalType[] terminals;
    private NonTerminalType[] nonTerminals;

    /**
     * For every class extends NonTerminalType replace every its instance in NonTerminalType::rules
     * with single one
     *
     * @param nonTerminals      list of non-terminals
     * @return                  new list
     */
    private static List<NonTerminalType> reduceNonTerminals(List<NonTerminalType> nonTerminals) {
        Map<NonTerminalType, NonTerminalType> types = new HashMap<>();
        nonTerminals.stream()
                .flatMap(nonTerminal -> Stream.concat(Stream.of(nonTerminal),
                        Arrays.stream(nonTerminal.getRules())
                                .flatMap(Arrays::stream)))
                .distinct()
                .filter(NonTerminalType.class::isInstance)
                .map(NonTerminalType.class::cast)
                .forEach(nonTerminal -> types.put(nonTerminal, nonTerminal.copy()));

        types.entrySet().forEach(entry -> {
            Type[][] rules = Arrays.stream(entry.getValue().getRules())
                    .map(rule -> Arrays.stream(rule)
                            .map(type -> {
                                if (type instanceof NonTerminalType)
                                    return types.get(type);
                                return type;
                            })
                            .toArray(Type[]::new))
                    .toArray(Type[][]::new);
            entry.getValue().setRules(rules);
        });

        return types.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Creates context free grammar by list of rules and start non-terminal
     *
     * @param nonTerminals      list of rules
     * @param start             start non-terminal
     */
    public Grammar(List<NonTerminalType> nonTerminals, NonTerminalType start) {
        startComparator = (nt1, nt2) -> nt1.equals(start) ? -1 : nt2.equals(start) ? 1 : 0;

        this.nonTerminals = reduceNonTerminals(nonTerminals).stream()
                .sorted(startComparator)
                .toArray(NonTerminalType[]::new);

        terminals = nonTerminals.stream()
                .flatMap(nonTerminal -> Arrays.stream(nonTerminal.getRules()))
                .flatMap(Arrays::stream)
                .distinct()
                .filter(TerminalType.class::isInstance)
                .map(TerminalType.class::cast)
                .toArray(TerminalType[]::new);
    }

    /**
     * Performs normalization into Chomsky normal form in 4 steps:
     *      1.  Add a new start non-terminal
     *      2.  Remove the long rules
     *      3.  Remove the (A -> bc, A -> bC, A -> Bc) rules
     *      4.  Remove the unit rules
     *
     * @return                  new grammar
     */
    public Grammar normalize() {
        List<NonTerminalType> normNonTerminals = reduceNonTerminals(Arrays.stream(nonTerminals)
                .collect(Collectors.toList()));
        normNonTerminals.sort(startComparator);

//        1
        NonTerminalType normStart = NonTerminalType.getTemp(new Type[][]{
                { normNonTerminals.get(0) }     //  old start non-terminal
        });
        normNonTerminals.add(normStart);

//        2
        for (int i = 0; i < normNonTerminals.size(); i++) {
            NonTerminalType nonTerminal = normNonTerminals.get(i);
            Type[][] rules = Arrays.stream(nonTerminal.getRules())
                    .map(rule -> shortenLongRule(rule, normNonTerminals))
                    .toArray(Type[][]::new);

            nonTerminal.setRules(rules);
        }

//        3
        for (int i = 0; i < normNonTerminals.size(); i++) {
            NonTerminalType nonTerminal = normNonTerminals.get(i);
            Type[][] rules = Arrays.stream(nonTerminal.getRules())
                    .map(rule -> removeRightTerminals(rule, normNonTerminals))
                    .toArray(Type[][]::new);

            nonTerminal.setRules(rules);
        }

//        4
        normNonTerminals.forEach(nonTerminal -> {
            Type[][] rules = Arrays.stream(nonTerminal.getRules())
                    .flatMap(rule -> Arrays.stream(removeUniqueRule(rule)))
                    .toArray(Type[][]::new);

            nonTerminal.setRules(rules);
        });

        return new Grammar(normNonTerminals, normStart);
    }

    private Type[] shortenLongRule(Type[] rule, List<NonTerminalType> normNonTerminals) {
        if (rule.length <= 2)
            return rule;

        Type[] rightPart = Arrays.copyOfRange(rule, 1, rule.length);
        NonTerminalType tmp = NonTerminalType.getTemp(new Type[][]{ rightPart });

        normNonTerminals.add(tmp);

        return new Type[]{ rule[0], tmp };
    }

    private Type[] removeRightTerminals(Type[] rule, List<NonTerminalType> normNonTerminals) {
        if (rule.length == 1)
            return rule;

        Type[] newRule = rule.clone();
        for (int i = 0; i < newRule.length; i++) {
            if (newRule[i] instanceof NonTerminalType)
                continue;

            newRule[i] = NonTerminalType.getTemp(new Type[][]{ { newRule[i] } });
            normNonTerminals.add((NonTerminalType) newRule[i]);
        }

        return newRule;
    }

    private Type[][] removeUniqueRule(Type[] rule) {
        if (rule.length > 1 || rule[0] instanceof TerminalType)
            return new Type[][]{ rule };

        return Arrays.stream(((NonTerminalType) rule[0]).getRules())
                .flatMap(__rule -> Arrays.stream(removeUniqueRule(__rule)))
                .toArray(Type[][]::new);
    }

    public TerminalType[] getTerminals() {
        return terminals;
    }

    public NonTerminalType[] getNonTerminals() {
        return nonTerminals;
    }

    public void print() {
        System.out.printf("Terminals:\n\t%s\n\n", Arrays.stream(terminals)
                .map(Type::toString)
                .collect(Collectors.joining("\n\t")));

        System.out.printf("%s\n\n\n", Arrays.stream(nonTerminals)
                .map(nonTerminal -> nonTerminal.toString() + "\n\t->\t" +
                        Arrays.stream(nonTerminal.getRules())
                                .map(Arrays::stream)
                                .map(classStream -> classStream
                                        .map(Type::toString)
                                        .collect(Collectors.joining(" ")))
                                .collect(Collectors.joining("\n\t->\t")))
                .collect(Collectors.joining("\n")));
    }
}
