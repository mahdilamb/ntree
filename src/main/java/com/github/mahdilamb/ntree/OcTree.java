package com.github.mahdilamb.ntree;

public class OcTree<T> extends NTreeRoot<T> {

    public OcTree(Box bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public OcTree(float[] min, float[] max, int capacity, int maxLevel) {
        this(new Box(min, max), capacity, maxLevel);
    }
    //TODO ray intersection
}
