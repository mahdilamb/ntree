package com.github.mahdilamb.ntree;

/**
 * A generic N-dimensional tree (e.g. Quadtree in 2 dimensions; Octree in 3 dimensions;
 * <p>
 * Note that "1 << x" is used instead of "2^x" (i.e. 2 to the power x).
 *
 * @param <T>
 * @author mahdi lamb
 */
public class NTreeNode<T> {
    public Box bound;
    public boolean isSelected = false;
    public T data;
    NTree<T> parent;

    public NTreeNode(Box bounds, T data) {
        this.bound = bounds;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("NTreeNode containing %s", data == null ? null : data.toString());
    }
}
