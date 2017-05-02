package ru.nsu.fit.g14203.popov.converter.optimisation;

import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.ParseTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Transformer {

    private List<Transformation> transformations = new ArrayList<>();

    public Transformer() { }

    public Transformer(Collection<Transformation> transformations) {
        this.transformations.addAll(transformations);
    }

    public void addOptimisation(Transformation transformation) {
        transformations.add(transformation);
    }

    /**
     * Performs all preset operations on given tree. Old reference may becomes invalid after this operation.
     * Always returns new reference if any optimisation have been performed.
     * Always returns old reference if no optimisation have been performed.
     *
     * @param tree                  tree to be optimised
     * @return                      new tree to replace old
     */
    public ParseTree transform(ParseTree tree) {
        boolean[] changed = { false };

        Node root = tree.getRoot();
        for (Transformation transformation : transformations) {
            root = transform(root, transformation, changed);
        }

        if (changed[0])
            return new ParseTree(root);
        return tree;
    }

    private Node transform(Node node, Transformation transformation, boolean[] changed) {
        List<Node> children = node.getChildren().stream()
                .map(child -> transform(child, transformation, changed))
                .collect(Collectors.toList());
        children.forEach(child -> child.setParent(node));
        node.setChildren(children);

        Node result = transformation.perform(node);
        if (result != node)
            changed[0] = true;

        return result;
    }
}
