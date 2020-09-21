package net.mahdilamb.illuminate.test;

public class OcTree<T> extends NTreeRoot<T> {

    public OcTree(CollidableAABB bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public OcTree(float[] min, float[] max, int capacity, int maxLevel) {
        this(new CollidableAABB(min, max), capacity, maxLevel);
    }
    //TODO ray intersection
}
