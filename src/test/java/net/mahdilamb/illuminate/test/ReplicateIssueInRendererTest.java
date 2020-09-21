package net.mahdilamb.illuminate.test;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class ReplicateIssueInRendererTest {
    @Test
    public void objectShouldBeFoundByCursorTest() {
        final QuadTree<Integer> test = new QuadTree<>(new float[]{-1, -1}, new float[]{+1, +1}, 2, 10);
        test.insert(
                new CollidableBox(new float[]{-1, 1}, new float[]{-1, 1}), 1
        );
        test.insert(
                new CollidableBox(new float[]{-0.975f, 0.96875f}, new float[]{-0.85f, 0.8125f}), 2
        );
        test.insert(
                new CollidableBox(new float[]{-1.0f, 1.0f}, new float[]{0.0f, -0.25f}), 3
        );
        assertFalse(test.getObjectsContaining(-0.90537083f, 0.8768719f).size() == 0);
    }

}
