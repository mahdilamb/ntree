package net.mahdilamb.ntree;

import java.util.List;

public class QuadTree<T> extends NTreeRoot<T> {
    public QuadTree(Box bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public QuadTree(float[] min, float[] max, int capacity, int maxLevel) {
        this(new Box(min, max), capacity, maxLevel);
    }

    public List<NTreeNode<T>> getObjectsContaining(final int x, final int y) {
        return getObjectsContaining(new Box(x, y, 0, 0));
    }
}
