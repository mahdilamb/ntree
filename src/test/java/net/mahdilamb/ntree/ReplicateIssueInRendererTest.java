package net.mahdilamb.ntree;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReplicateIssueInRendererTest {
    @Test
    public void objectShouldBeFoundByCursorTest() {
        final QuadTree<Integer> test = new QuadTree<>(new float[]{-1, -1}, new float[]{+1, +1}, 4, 50);

        test.add(
                new CollidableAABB(new float[]{-0.975f, 0.96875f}, new float[]{-0.85f, 0.8125f}), 2
        );
        test.add(
                new CollidableAABB(new float[]{-1.0f, 1.0f}, new float[]{0.0f, -0.25f}), 3
        );
        test.add(
                new CollidableAABB(new float[]{0.0f, 1.0f}, new float[]{0.0f, -0.25f}), 5
        );
        assertEquals(test.getObjectsContaining(-0.9207161f, 0.8735441f).size(), 2);
    }

}
