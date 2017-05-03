package ru.nsu.fit.g14203.popov.parse;

@FunctionalInterface
public interface Transformation {

    /**
     * Performs transformation on node. Old reference may become invalid after that operation.
     * Always returns new reference if transformation has been performed.
     * Always returns old reference if transformation hasn't been performed.
     *
     * @param node                  node to be transformed
     * @return                      new node to replace old
     */
    Node perform(Node node);
}
