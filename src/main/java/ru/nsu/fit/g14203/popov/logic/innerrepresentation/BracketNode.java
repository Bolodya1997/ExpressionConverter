package ru.nsu.fit.g14203.popov.logic.innerrepresentation;

import ru.nsu.fit.g14203.popov.logic.terminal.CloseBracket;
import ru.nsu.fit.g14203.popov.logic.terminal.OpenBracket;
import ru.nsu.fit.g14203.popov.parse.Node;

public class BracketNode extends Node {

    private final static String OPEN_BRACKET    = new OpenBracket().getDefaultValue();
    private final static String CLOSE_BRACKET   = new CloseBracket().getDefaultValue();

    public BracketNode() {
        super(new InnerType());
    }

    @Override
    public Node copy() {
        Node result = new BracketNode();
        getChildren().forEach(child -> result.getChildren().add(child.copy()));

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s", OPEN_BRACKET, super.toString(), CLOSE_BRACKET);
    }
}
