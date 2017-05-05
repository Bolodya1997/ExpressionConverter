package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.NonTerminalType;
import ru.nsu.fit.g14203.popov.parse.types.Terminal;
import ru.nsu.fit.g14203.popov.parse.types.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParseTree {

    class TreeException extends Exception {
        int position;

        TreeException(int position) {
            this.position = position;
        }
    }

    private Node root;

    ParseTree(Node root) {
        this.root = root;
        root.setParent(null);
    }

    /**
     * Build parse tree to given terminal sequence using CYK algorithm with replacement error
     * recognition.
     * Grammar must be given in Chomsky normal form with start non-terminal at first place.
     *
     * @param grammar               context-free grammar to define the language
     * @param sequence              terminal sequence
     *
     * @throws TreeException        if sequence is can't be parsed, with position at first error
     */
    ParseTree(Grammar grammar, Terminal[] sequence) throws TreeException {
        NonTerminalType[] nonTerminals = grammar.getNonTerminals();

        buildTree(nonTerminals, sequence);  //  build tree in normalized grammar
        shortenTree(root);                  //  remove temporary nodes
        addParent(root);                    //  add parent to every node (except root)
    }

    private void buildTree(NonTerminalType[] nonTerminals, Terminal[] sequence)
            throws TreeException {
        if (sequence.length == 0)
            throw new TreeException(-1);

        Node[] terminals = Arrays.stream(sequence)
                .map(Node::new)
                .toArray(Node[]::new);

        Map<NonTerminalType, Integer> positionsMap = new HashMap<>();
        for (int i = 0; i < nonTerminals.length; i++) {
            positionsMap.put(nonTerminals[i], i);
        }

        /*
         *  Define:
         *      derivation<0> := correct derivation
         *      derivation<k> := derivation with k replacement mistakes
         *
         *  Example:
         *      rules:
         *          A -> a | aA
         *          B -> bA | bB
         *      derivations:
         *          A -> aaa - derivation<0>
         *          A -> aab - derivation<1> (aaa)
         *          B -> aab - derivation<2> (baa)
         */

        /*
         * For every not null (entry = table[i][j][nt]):
         *      nonTerminals[nt] -> sequence[i : j] - derivation<entry.weight>
         */
        Node[][][] table = new Node[sequence.length][sequence.length][nonTerminals.length];

        /*
         * For every (terminal, nonTerminal):
         *      terminal                sequence[i]
         *      nonTerminal             nonTerminals[nt]
         *
         * Add possible leaf to the parse tree:
         *      table[i][i][nt] = new Node(nonTerminal);
         *      table[i][i][nt].children = { terminal };
         *
         * If rule (nonTerminal -> terminal) exists:
         *      table[i][i][nt].weight = 0;
         * else:
         *      table[i][i][nt].weight = 1;
         */
        for (int i = 0; i < sequence.length; i++) {
            for (int nt = 0; nt < nonTerminals.length; nt++) {
                Node terminal = terminals[i];

                int weight = 0;
                if (Arrays.stream(nonTerminals[nt].getRules())
                        .noneMatch(rule -> rule[0].equals(terminal.getType()))) {
                    weight = 1;
                }

                table[i][i][nt] = new Node(nonTerminals[nt]).setWeight(weight);
                table[i][i][nt].getChildren().add(terminal);
            }
        }

        /*
         * For every (subsequence, m, nt):
         *      subsequence             sequence[i : j]
         *      derivations             nonTerminals[left] -> sequence[i : m]
         *                              nonTerminals[right] -> sequence[m + 1 : j]
         *      rule                    nonTerminals[nt] -> nonTerminals[left] nonTerminals[right]
         *
         * Add possible node to the parse tree:
         *      table[i][j][nt] = new Node(nonTerminals[nt]);
         *      table[i][j][nt].children = { table[i][m][left], table[m + 1][j][right] };
         *
         * nonTerminals[nt] -> subsequence - derivation<weight>, where:
         *      lWeight = table[i][m][left].weight;
         *      rWeight = table[i][m][right].weight;
         *      weight = min<for all (left, right) pairs>(lWeight + rWeight);
         */
        for (int length = 1; length < sequence.length; length++) {
            for (int i = 0; i < sequence.length - length; i++) {
                int j = i + length;
ntLoop:         for (int nt = 0; nt < nonTerminals.length; nt++) {
                    for (int m = i; m < j; m++) {

                        for (Type[] rule : nonTerminals[nt].getRules()) {
                            if (rule.length == 1)
                                continue;

                            int left = positionsMap.get(rule[0]);
                            if (table[i][m][left] == null)
                                continue;

                            int right = positionsMap.get(rule[1]);
                            if (table[m + 1][j][right] == null)
                                continue;

                            int weight = table[i][m][left].getWeight() +
                                         table[m + 1][j][right].getWeight();
                            if (table[i][j][nt] == null || table[i][j][nt].getWeight() > weight) {
                                table[i][j][nt] = new Node(nonTerminals[nt]);
                                table[i][j][nt].getChildren().add(table[i][m][left]);
                                table[i][j][nt].getChildren().add(table[m + 1][j][right]);
                            }

                            if (table[i][j][nt].getWeight() == 0)
                                continue ntLoop;
                        }
                    }
                }
            }
        }

        /*
         * If root is not null:
         *      startNonTerminal -> sequence - derivation<root.weight>
         */
        root = table[0][sequence.length - 1][0];
        if (root.getWeight() == 0)
            return;

        Node[] leaves = root.getLeaves()
                .toArray(Node[]::new);
        for (int i = 0; i < leaves.length; i++) {
            Node leaf = leaves[i];
            if (leaf.getWeight() != 0)
                throw new TreeException(i);
        }
    }

    private Stream<Node> shortenTree(Node node) {
        if (node.getChildren().size() == 1)
            return node.getChildren().stream();

        List<Node> children = node.getChildren().stream()
                .flatMap(this::shortenTree)
                .collect(Collectors.toList());
        node.setChildren(children);

        if (((NonTerminalType) node.getType()).isTemp())
            return children.stream();
        return Stream.of(node);
    }

    private void addParent(Node node) {
        node.getChildren().forEach(child -> {
            child.setParent(node);
            addParent(child);
        });
    }

    Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
