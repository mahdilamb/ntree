package net.mahdilamb.ntree;

import java.util.List;

public class QuadTree<T> extends NTreeRoot<T> {
    public QuadTree(CollidableAABB bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public QuadTree(float[] min, float[] max, int capacity, int maxLevel) {
        this(new CollidableAABB(min, max), capacity, maxLevel);
    }

    public List<NTreeNode<T>> getObjectsContaining(final float x, final float y) {
        return getObjectsContaining(new CollidableAABB(x, y, 0, 0));
    }
    @Override
    public String toString(){
        System.out.println(allObjects);
        return super.toString();
    }
}
