package ru.nsu.fit.g14203.popov.converter.optimisation;

import ru.nsu.fit.g14203.popov.parse.Node;

@FunctionalInterface
public interface Optimisation {

    /**
     * Performs optimisation on node. Old reference may become invalid after that operation.
     * Always returns new reference if optimisation has been performed.
     * Always returns old reference if optimisation hasn't been performed.
     *
     * @param node                  node to be optimised
     * @return                      new node to replace old
     */
    Node perform(Node node);
}
