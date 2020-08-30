package net.mahdilamb.ntree;


import java.util.List;
import java.util.Vector;

/**
 * A generic N-dimensional tree (e.g. Quadtree in 2 dimensions; Octree in 3 dimensions;
 * <p>
 * Note that "1 << x" is used instead of "2^x" (i.e. 2 to the power x).
 *
 * @param <T>
 * @author mahdi lamb
 */

public class Ntree<T> {
    final List<Ntree<T>> children = new Vector<>();
    final List<NTreeNode> objects = new Vector<>();
    final List<NTreeNode> foundObjects = new Vector<>();
    int capacity;
    int maxLevel;
    Box bounds;
    Ntree<T> parent;
    int numDimensions;
    private boolean isLeaf = true;
    private int level = 0;
    public Ntree(final Box bound, final int capacity, final int maxLevel) {
        this.bounds = bound;
        this.capacity = capacity;
        this.maxLevel = maxLevel;
        this.numDimensions = bound.numDimensions();
        for (int i = 0; i < (1 << numDimensions); i++) {
            children.add(null);
        }
    }
    public Ntree(float[] min, float[] max, final int capacity, final int maxLevel) {
        this(new Box(min, max),capacity, maxLevel);

    }

    public Ntree(final Ntree<T> other) {
        this(other.getBounds(), other.capacity, other.maxLevel);
    }

    private void subdivide() {

        final float[] min = new float[numDimensions];
        final float[] newLengths = new float[numDimensions];
        for (int d = 0; d < numDimensions; d++) {
            min[d] = (float) getBounds().realMin(d);
            newLengths[d] = (float) ((getBounds().realMax(d) - getBounds().realMin(d)) * 0.5f);
        }
        for (int i = 0; i < (1 << numDimensions); i++) {
            final float[] thisMin = min.clone();
            final float[] thisMax = new float[numDimensions];
            for (int e = 0; e < numDimensions; e++) {
                if ((i & (1 << e)) == e + 1) {
                    thisMin[e] += newLengths[e];
                }
                thisMax[e] = thisMin[e] + newLengths[e];
            }

            final Ntree<T> newNode = new Ntree<>(new Box(thisMin, thisMax), capacity, maxLevel);
            newNode.level = level + 1;
            newNode.parent = this;
            children.set(i, newNode);

        }
        isLeaf = false;
    }

    private Ntree<T> getChild(final Collidable bound) {
        int i = 0;
        for (int d = 0; d < numDimensions; d++) {
            final float center = (float) ((getBounds().realMin(d) + getBounds().realMax(d)) * .5f);
            if (bound.realMax(d) > center && bound.realMin(d) < center) {
                return null;
            }
            if (bound.realMin(d) > center) {
                i |= 1 << d;
            }
        }
        return children.get(i);
    }

    private void discardEmptyBuckets() {
        if (objects.size() == 0) return;
        if (!isLeaf) {
            for (final Ntree<T> child : children)
                if (!child.isLeaf || child.objects.size() > 0)
                    return;
        }
        clear();
        if (parent != null)
            parent.discardEmptyBuckets();
    }

    public boolean insert(final NTreeNode obj) {
        if (obj.bound.numDimensions() != numDimensions) {
            throw new IllegalArgumentException("Can only insert objects of the same number of dimension");
        }
        if (obj.parent != null) {
            return false;
        }
        if (!isLeaf) {
            final Ntree<T> child = getChild(obj.bound);
            if (child != null) {
                return child.insert(obj);
            }
        }
        objects.add(obj);
        obj.parent = this;

        if (isLeaf && level < maxLevel && objects.size() >= capacity) {
            subdivide();
            update(obj);
        }
        return true;
    }

    public boolean insert(final Collidable bounds, T object) {

        return insert(new NTreeNode(bounds, object));

    }

    public boolean remove(final NTreeNode obj) {
        if (obj.parent == null) {
            return false;
        }
        if (obj.parent != this) {
            return obj.parent.remove(obj);
        }
        objects.remove(obj);
        obj.parent = null;
        discardEmptyBuckets();
        return true;
    }

    public boolean update(final NTreeNode obj) {
        if (!remove(obj)) return false;

        if (parent != null && !getBounds().containsBoundingBox(obj.bound))
            return parent.insert(obj);
        if (!isLeaf) {
            final Ntree<T> child = getChild(obj.bound);
            if (child != null) {
                return child.insert(obj);
            }

        }
        return insert(obj);
    }

    public List<NTreeNode> getObjectsContaining(final Collidable bound) {
        foundObjects.clear();
        for (final NTreeNode obj : objects) {
            if (obj.bound != bound && obj.bound.containsBoundingBox(bound))
                foundObjects.add(obj);
        }

        if (!isLeaf) {
            final Ntree<T> child = getChild(bound);

            if (child != null) {
                child.getObjectsContaining(bound);
                foundObjects.addAll(child.foundObjects);
            } else {
                for (final Ntree<T> leaf : children) {

                    if (leaf.getBounds().containsBoundingBox(bound)) {
                        leaf.getObjectsContaining(bound);
                        foundObjects.addAll(leaf.foundObjects);
                    }
                }
            }
        }
        return foundObjects;
    }

    public List<NTreeNode> getObjectsInBound(final Collidable bound) {
        foundObjects.clear();
        for (final NTreeNode obj : objects) {
            if (obj.bound != bound && obj.bound.intersectsBoundingBox(bound))
                foundObjects.add(obj);
        }

        if (!isLeaf) {
            final Ntree<T> child = getChild(bound);

            if (child != null) {
                child.getObjectsInBound(bound);
                foundObjects.addAll(child.foundObjects);
            } else {
                for (final Ntree<T> leaf : children) {
                    if (leaf.getBounds().intersectsBoundingBox(bound)) {
                        leaf.getObjectsInBound(bound);
                        foundObjects.addAll(leaf.foundObjects);
                    }
                }
            }
        }
        return foundObjects;
    }

    public int totalChildren() {
        int total = 0;
        if (isLeaf) return total;
        for (final Ntree<T> child : children)
            total += child.totalChildren();
        return ((1 << numDimensions) + total);
    }

    public int totalObjects() {
        int total = objects.size();
        if (!isLeaf) {
            for (final Ntree<T> child : children)
                total += child.totalObjects();
        }
        return total;

    }

    public void clear() {
        if (objects.size() != 0) {
            for (final NTreeNode obj : objects) {
                obj.parent = null;
            }
            objects.clear();
        }
        if (!isLeaf) {
            for (final Ntree<T> child : children) {
                child.clear();
            }
            isLeaf = true;
        }
    }

    public List<Ntree<T>> getChildren() {
        return children;
    }

    public List<NTreeNode> getObjectsContaining(final int x, final int y) {
        return getObjectsContaining(new Box(x, y, 0, 0));
    }

    public Box getBounds() {
        return bounds;
    }

    public class NTreeNode {
        public Collidable bound;
        public boolean isSelected = false;
        public T data;
        Ntree<T> parent;

        public NTreeNode(Collidable bounds, T data) {
            this.bound = bounds;
            this.data = data;
        }
    }
}
