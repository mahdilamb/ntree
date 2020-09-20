package net.mahdilamb.ntree;

import java.util.HashMap;
import java.util.Map;

public class NTreeRoot<T> extends NTree<T> {
    public Map<T, NTreeNode<T>> allObjects = new HashMap<>();

    public NTreeRoot(Box bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public NTreeRoot(float[] min, float[] max, int capacity, int maxLevel) {
        this(new Box(min, max), capacity, maxLevel);
    }

    public NTreeNode<T> getNode(T object) {
        return allObjects.get(object);
    }

    public void update(T object, Box newBoundingBox) {
        final NTreeNode<T> node = getNode(object);
        assert node.bound.numDimensions() == newBoundingBox.numDimensions();
        for (int i = 0; i < node.bound.min.length; i++) {
            node.bound.min[i] = newBoundingBox.min[i];
            node.bound.max[i] = newBoundingBox.max[i];
        }
        update(node);
    }

    @Override
    public boolean insert(final NTreeNode<T> obj) {
        if (super.insert(obj)) {
            allObjects.put(obj.data, obj);
            return true;
        }
        return false;
    }

    public boolean remove(final NTreeNode<T> obj) {
        if (super.remove(obj)) {
            allObjects.remove(obj.data);
            return true;
        }
        return false;
    }
}
