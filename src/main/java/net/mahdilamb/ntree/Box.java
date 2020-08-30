package net.mahdilamb.ntree;


public class Box implements Collidable {
    final float[] min;
    final float[] max;

    public Box(float[] min, float[] max) {

        assert min.length == max.length;
        this.min = min;
        this.max = max;
    }

    public Box(float x, float y, float width, float height) {
        this(new float[]{x, y}, new float[]{x + width, y + height});
    }

    public Box(float x, float y, float z, float width, float height, float depth) {
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
}
