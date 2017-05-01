package ru.nsu.fit.g14203.popov.converter.optimisation;

import ru.nsu.fit.g14203.popov.parse.Node;
import ru.nsu.fit.g14203.popov.parse.ParseTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Optimiser {

    private List<Optimisation> optimisations = new ArrayList<>();

    public Optimiser() { }

    public Optimiser(Collection<Optimisation> optimisations) {
        this.optimisations.addAll(optimisations);
    }

    public void addOptimisation(Optimisation optimisation) {
        optimisations.add(optimisation);
    }

    /**
     * Performs all preset operations on given tree. Old reference may becomes invalid after this operation.
     * Always returns new reference if any optimisation have been performed.
     * Always returns old reference if no optimisation have been performed.
     *
     * @param tree                  tree to be optimised
     * @return                      new tree to replace old
     */
    public ParseTree optimise(ParseTree tree) {
        boolean[] changed = { false };

        Node root = tree.getRoot();
        for (Optimisation optimisation : optimisations) {
            root = optimise(root, optimisation, changed);
        }

        if (changed[0])
            return new ParseTree(root);
        return tree;
    }

    private Node optimise(Node node, Optimisation optimisation, boolean[] changed) {
        List<Node> children = node.getChildren().stream()
                .map(child -> optimise(child, optimisation, changed))
                .collect(Collectors.toList());
        children.forEach(child -> child.setParent(node));
        node.setChildren(children);

        Node result = optimisation.perform(node);
        if (result != node)
            changed[0] = true;

        return result;
    }
}
