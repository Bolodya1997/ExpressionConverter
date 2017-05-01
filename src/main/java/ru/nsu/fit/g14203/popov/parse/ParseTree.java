package ru.nsu.fit.g14203.popov.parse;

import ru.nsu.fit.g14203.popov.parse.types.Terminal;

public class ParseTree {

    private Node root;

    public ParseTree(Grammar grammar, Terminal[] word) throws ParseException {
        ParseTreeBuilder builder = new ParseTreeBuilder(grammar, word);
        root = builder.build();
    }

    public ParseTree(Node root) {
        this.root = root;
        root.setParent(null);
    }

    public Node getRoot() {
        return root;
    }

    public void print() {
        System.out.println(root);
    }
}
