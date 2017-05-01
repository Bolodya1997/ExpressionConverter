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

class ParseTreeBuilder {

    private Node root;

    private NonTerminalType[] nonTerminals;
    private Terminal[] word;

    ParseTreeBuilder(Grammar grammar, Terminal[] word) {
        nonTerminals = grammar.normalize().getNonTerminals();
        this.word = word;
    }

    Node build() throws ParseException {
        buildTree();
        shortenTree(root);
        addParent(root);

        return root;
    }

    /**
     * Build parse tree using CYK algorithm
     */
    private void buildTree() throws ParseException {
        Node[] leaves = Arrays.stream(word)
                .map(Node::new)
                .toArray(Node[]::new);

        Map<NonTerminalType, Integer> positionsMap = new HashMap<>();
        for (int i = 0; i < nonTerminals.length; i++) {
            positionsMap.put(nonTerminals[i], i);
        }

        Node[][][] table = new Node[word.length][word.length][nonTerminals.length];
        for (int i = 0; i < word.length; i++) {
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

        for (int l = 1; l < word.length; l++) {                             //  subsequence length
            for (int i = 0; i < word.length - l; i++) {                     //  from
                int j = i + l;                                              //  to
ntLoop:         for (int pos = 0; pos < nonTerminals.length; pos++) {
                    for (int k = i; k < j; k++) {                           //  left part ends

                        for (Type[] rule : nonTerminals[pos].getRules()) {
                            if (rule.length == 1)
                                continue;

                            int left = positionsMap.get(rule[0]);
                            if (table[i][k][left] == null)
                                continue;

                            int right = positionsMap.get(rule[1]);
                            if (table[k + 1][j][right] == null)
                                continue;

                            table[i][j][pos] = new Node(nonTerminals[pos]);
                            table[i][j][pos].getChildren().add(table[i][k][left]);
                            table[i][j][pos].getChildren().add(table[k + 1][j][right]);

                            continue ntLoop;
                        }
                    }
                }
            }
        }

        root = table[0][word.length - 1][0];
        if (root == null)
            throw new ParseException("Non-parseable word");
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
}
