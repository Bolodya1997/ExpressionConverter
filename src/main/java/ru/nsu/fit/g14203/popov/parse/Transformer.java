package ru.nsu.fit.g14203.popov.parse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class Transformer {

    /**
     * Performs all transformations on each node of given tree. Old reference may become invalid
     * after this operation.
     *
     * Always returns new reference if any transformation have been performed.
     * Always returns old reference if no transformations have been performed.
     *
     * @param tree                  tree to be transformed
     * @param transformations       transformations to perform on tree
     * @return                      new tree to replace old
     */
    static ParseTree transform(ParseTree tree, Collection<Transformation> transformations) {
        boolean[] changed = { false };

        Node root = tree.getRoot();
        for (Transformation transformation : transformations) {
            root = transform(root, transformation, changed);
        }

        if (changed[0])
            return new ParseTree(root);
        return tree;
    }

    private static Node transform(Node node, Transformation transformation, boolean[] changed) {
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
