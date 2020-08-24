package net.mahdilamb.ntree;

import net.imglib2.RealInterval;

public interface Collidable extends RealInterval {
    default boolean containsBoundingBox(Collidable other) {
        if (other.numDimensions() != numDimensions()) {
            throw new IllegalArgumentException("cannot perform intersection on objects with different number of dimensions");
        }
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMin(d) || realMax(d) < other.realMax(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean intersectsBoundingBox(Collidable other) {
        if (other.numDimensions() != numDimensions()) {
            throw new IllegalArgumentException("cannot perform intersection on objects with different number of dimensions");

        }
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMax(d) || realMax(d) < other.realMin(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsPoint(double[] coords) {
        assert coords.length == 2 || coords.length == 3;
        final double[] max = coords.clone();
        for (int i = 0; i < max.length; i++) {
            max[i] += .5;
        }
        return containsBoundingBox(new Box(coords, max));
    }

    default boolean intersectsRay(double[] origin, double[] direction) {
        assert origin.length == 3;
        assert direction.length == 3;
        final double[] dirfrac = new double[3];
        dirfrac[0] = 1.0d / direction[0];
        dirfrac[1] = 1.0d / direction[1];
        dirfrac[2] = 1.0d / direction[2];

        final double t1 = (realMin(0) - origin[0]) * dirfrac[0];
        final double t2 = (realMax(0) - origin[0]) * dirfrac[0];
        final double t3 = (realMin(1) - origin[1]) * dirfrac[1];
        final double t4 = (realMax(1) - origin[1]) * dirfrac[1];
        final double t5 = (realMin(2) - origin[2]) * dirfrac[2];
        final double t6 = (realMax(2) - origin[2]) * dirfrac[2];

        final double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        final double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0) {
            //t = tmax;
            return false;
        }

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {
            //t = tmax;
            return false;
        }

        //t = tmin;
        return true;
    }
}
