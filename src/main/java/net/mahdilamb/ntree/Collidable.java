package net.mahdilamb.ntree;

public interface Collidable {
    int numDimensions();

    double realMin(int d);

    double realMax(int d);

    default boolean containsBoundingBox(Collidable other) {
        assert  (other.numDimensions() == numDimensions()) ;
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMin(d) || realMax(d) < other.realMax(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean intersectsBoundingBox(Collidable other) {
        assert  (other.numDimensions() == numDimensions()) ;
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMax(d) || realMax(d) < other.realMin(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsPoint(float[] coords) {
        return containsBoundingBox(new CollidableAABB(coords, coords));
    }
    /*
    default Float rayIntersectAABB(float[] origin, float[] direction) {
        assert origin.length == 3;
        assert direction.length == 3;
        final float[] dirFrac = new float[3];
        dirFrac[0] = 1.0f / direction[0];
        dirFrac[1] = 1.0f / direction[1];
        dirFrac[2] = 1.0f / direction[2];

        final float t1 = (float) ((realMin(0) - origin[0]) * dirFrac[0]);
        final float t2 = (float) ((realMax(0) - origin[0]) * dirFrac[0]);
        final float t3 = (float) ((realMin(1) - origin[1]) * dirFrac[1]);
        final float t4 = (float) ((realMax(1) - origin[1]) * dirFrac[1]);
        final float t5 = (float) ((realMin(2) - origin[2]) * dirFrac[2]);
        final float t6 = (float) ((realMax(2) - origin[2]) * dirFrac[2]);

        final float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0) {
            return null;

        }

        final float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {
            //t = tmax;
            return null;
        }

        //t = tmin;
        return tmin;
    }*/
}
