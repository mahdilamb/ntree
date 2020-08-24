package net.mahdilamb.ntree;

import net.imglib2.roi.geom.real.ClosedWritableBox;

public class Box extends ClosedWritableBox implements Collidable {

    public Box(double[] min, double[] max) {
        super(min, max);
        assert min.length == max.length;

    }

    public Box(double x, double y, double width, double height) {
        this(new double[]{x, y}, new double[]{x + width, y + height});
    }

    public Box(double x, double y, double z, double width, double height, double depth) {
        this(new double[]{x, y, z}, new double[]{x + width, y + height, z + depth});
    }

}
