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

    private Node root;

    ParseTree(Node root) {
        this.root = root;
        root.setParent(null);
    }

    /**
     * Build parse tree to given terminal sequence using CYK algorithm.
     * Grammar must be given in Chomsky normal form with start non-terminal at first place.
     *
     * @param grammar               context-free grammar to define the language
     * @param sequence              terminal sequence
     *
     * @throws ParseException       if sequence doesn't contain in defined language
     */
    ParseTree(Grammar grammar, Terminal[] sequence) throws ParseException {
        NonTerminalType[] nonTerminals = grammar.getNonTerminals();

        buildTree(nonTerminals, sequence);  //  build tree in normalized grammar (have temporary nodes)
        shortenTree(root);                  //  remove temporary nodes
        addParent(root);                    //  add parent to every node (except root)
    }

    private void buildTree(NonTerminalType[] nonTerminals, Terminal[] sequence) throws ParseException {
        if (sequence.length == 0)
            throw new ParseException("Bad sequence", 0, ParseException.Reason.TREE);

        Node[] leaves = Arrays.stream(sequence)
                .map(Node::new)
                .toArray(Node[]::new);

        Map<NonTerminalType, Integer> positionsMap = new HashMap<>();
        for (int i = 0; i < nonTerminals.length; i++) {
            positionsMap.put(nonTerminals[i], i);
        }

        /*
         * For every not null table[i][j][nt], derivation:
         *      nonTerminals[nt] -> sequence[i : j] ~ (sequence[i] sequence[i + 1] ... sequence[j])
         * must be correct.
         */
        Node[][][] table = new Node[sequence.length][sequence.length][nonTerminals.length];

        /*
         * For every:
         *      terminal                sequence[i]
         *      rule                    nonTerminals[nt] -> terminal
         *
         * Add possible leaf to the parse tree:
         *      table[i][i][nt] = new Node(nonTerminals[nt]);
         */
        for (int i = 0; i < sequence.length; i++) {
            for (int nt = 0; nt < nonTerminals.length; nt++) {
                Node leaf = leaves[i];

                if (Arrays.stream(nonTerminals[nt].getRules())
                        .noneMatch(rule -> rule[0].equals(leaf.getType()))) {
                    continue;
                }

                table[i][i][nt] = new Node(nonTerminals[nt]);
                table[i][i][nt].getChildren().add(leaf);
            }
        }

        /*
         * For first pair (left, right):
         *      subsequence             sequence[i : j]
         *      correct derivations     nonTerminals[left] -> sequence[i : m]
         *                              nonTerminals[right] -> sequence[m + 1 : j]
         *      rule                    nonTerminals[nt] -> nonTerminals[left] nonTerminals[right]
         *
         * Add possible node to the parse tree:
         *      table[i][j][nt] = new Node(nonTerminals[nt]);
         *      table[i][j][nt].children = { table[i][m][left], table[m + 1][j][right] };
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

                            table[i][j][nt] = new Node(nonTerminals[nt]);
                            table[i][j][nt].getChildren().add(table[i][m][left]);
                            table[i][j][nt].getChildren().add(table[m + 1][j][right]);

                            continue ntLoop;
                        }
                    }
                }
            }
        }

        /*
         * If root is not null, derivation:
         *      startNonTerminal -> sequence
         * is correct.
         */
        root = table[0][sequence.length - 1][0];
        if (root == null) {     //  FIXME: find correct error position
            for (int i = 1; i < sequence.length; i++) {
                if (table[0][i][0] == null)
                    throw new ParseException(sequence[i].getValue(), i, ParseException.Reason.TREE);
            }
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
