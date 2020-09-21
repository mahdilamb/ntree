package net.mahdilamb.illuminate.test;


import java.util.Arrays;

public class CollidableAABB implements Collidable {
    final float[] min;
    final float[] max;

    public CollidableAABB(float[] min, float[] max) {
        assert min.length == max.length;
        this.min = new float[min.length];
        this.max = new float[max.length];
        for (int d = 0; d < min.length; d++) {
            this.min[d] = Math.min(max[d], min[d]);
            this.max[d] = Math.max(max[d], min[d]);
        }
    }

    public CollidableAABB(float x, float y, float width, float height) {
        this(new float[]{x, y}, new float[]{x + width, y + height});
    }

    public CollidableAABB(float x, float y, float z, float width, float height, float depth) {
        this(new float[]{x, y, z}, new float[]{x + width, y + height, z + depth});
    }

    @Override
    public int numDimensions() {
        return min.length;
    }

    @Override
    public double realMin(int d) {
        return min[d];
    }

    @Override
    public double realMax(int d) {
        return max[d];
    }

    @Override
    public String toString() {
        return String.format("Collidable box between %s and  %s", Arrays.toString(min), Arrays.toString(max));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Collidable)) {
            return false;
        }
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) != ((Collidable) o).realMin(d)) {
                return false;
            }
            if (realMax(d) != ((Collidable) o).realMax(d)) {
                return false;
            }
        }
        return true;
    }
}
