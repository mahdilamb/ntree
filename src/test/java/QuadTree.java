import net.mahdilamb.ntree.Box;
import net.mahdilamb.ntree.Ntree;
import net.mahdilamb.ntree.Ntree.NTreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

class GraphicsPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    final int[] selection = new int[4];
    boolean hasSelection = false;

    GraphicsPanel() {
    }

    private void drawBounds(Graphics g, Ntree quadTree) {
        if (quadTree.getChildren().get(0) == null) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            g.drawRect((int) ((Ntree) quadTree.getChildren().get(i)).getBounds().realMin(0),
                    (int) ((Ntree) quadTree.getChildren().get(i)).getBounds().realMin(1),
                    (int) (((Ntree) quadTree.getChildren().get(i)).getBounds().realMax(0)
                            - ((Ntree) quadTree.getChildren().get(i)).getBounds().realMin(0)),
                    (int) (((Ntree) quadTree.getChildren().get(i)).getBounds().realMax(1)
                            - ((Ntree) quadTree.getChildren().get(i)).getBounds().realMin(1)));
            drawBounds(g, (Ntree) quadTree.getChildren().get(i));

        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(java.awt.Color.white);

        drawBounds(g, QuadTree.quadTree);
        final List<Ntree<Integer>.NTreeNode> objects = QuadTree.quadTree
                .getObjectsInBound(new Box(0, 0, QuadTree.width, QuadTree.height));
        objects.sort(new Comparator<NTreeNode>() {

            @Override
            public int compare(NTreeNode o1, NTreeNode o2) {
                return ((Boolean) o1.isSelected).compareTo(o2.isSelected);
            }

        });

        for (NTreeNode obj : objects) {
            g.setColor(obj.isSelected ? java.awt.Color.green : java.awt.Color.red);
            g.fillRect((int) obj.bound.realMin(0), (int) obj.bound.realMin(1),
                    (int) (obj.bound.realMax(0) - obj.bound.realMin(0)),
                    (int) (obj.bound.realMax(1) - obj.bound.realMin(1)));

        }

        if (hasSelection) {
            g.setColor(java.awt.Color.red);
            g.fillRect(Math.min(selection[0], selection[2]), Math.min(selection[1], selection[3]),
                    Math.abs(selection[2] - selection[0]), Math.abs(selection[3] - selection[1]));
            g.setColor(java.awt.Color.black);
            g.drawRect(Math.min(selection[0], selection[2]), Math.min(selection[1], selection[3]),
                    Math.abs(selection[2] - selection[0]), Math.abs(selection[3] - selection[1]));
        }
    }

}

public class QuadTree {
    final static JFrame frame = new JFrame();
    final static GraphicsPanel panel = new GraphicsPanel();
    final static List<NTreeNode> selected = new Vector<NTreeNode>();
    final static int width = 500;
    final static int height = 500;
    final static Ntree<Integer> quadTree = new Ntree<Integer>(new Box(0, 0, width, height), 1, 10);

    static void addRect(final Box rect) {
        quadTree.insert(rect, null);
        if (frame.isVisible()) {
            frame.setTitle("total objects: " + quadTree.totalObjects());
            panel.repaint();
        }

    }

    public static void main(String... args) {

        MouseAdapter drawTool = new MouseAdapter() {
            private int[] startCoords = new int[2];
            private boolean isDragging = false;

            @Override
            public void mouseDragged(MouseEvent e) {
                isDragging = true;
                if (panel.hasSelection) {
                    panel.selection[2] = e.getX();
                    panel.selection[3] = e.getY();

                }
                panel.repaint();

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                final List<Ntree<Integer>.NTreeNode> matchedObjects = quadTree.getObjectsContaining(e.getX(), e.getY());
                selected.parallelStream().forEach((selectedObj) -> {
                    selectedObj.isSelected = false;
                });

                selected.clear();

                for (NTreeNode obj : matchedObjects) {
                    obj.isSelected = true;
                    selected.add(obj);

                }

                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isDragging = false;
                startCoords[0] = e.getX();
                startCoords[1] = e.getY();
                panel.selection[0] = e.getX();
                panel.selection[1] = e.getY();
                panel.selection[2] = e.getX();
                panel.selection[3] = e.getY();
                panel.hasSelection = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                panel.hasSelection = false;
                if (isDragging == false) {
                    return;
                }
                final int x1 = startCoords[0] > e.getX() ? startCoords[0] : e.getX();
                final int y1 = startCoords[1] > e.getY() ? startCoords[1] : e.getY();
                final int x2 = startCoords[0] <= e.getX() ? startCoords[0] : e.getX();
                final int y2 = startCoords[1] <= e.getY() ? startCoords[1] : e.getY();
                addRect(new Box(x2, y2, x1 - x2, y1 - y2));

            }

        };
        panel.addMouseListener(drawTool);
        panel.addMouseMotionListener(drawTool);

        /*
         * for(int i = 0; i < 1000;i++) { addRect(new
         * Box(RandomUtils.nextInt(0,500),RandomUtils.nextInt(0,500),RandomUtils.nextInt
         * (0,100),RandomUtils.nextInt(0,100))); }
         */
        addRect(new Box(10, 10, 10, 10));
        addRect(new Box(260, 10, 10, 10));
        addRect(new Box(260, 260, 10, 10));
        addRect(new Box(10, 260, 10, 10));

        frame.setContentPane(panel);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
        frame.setTitle("total objects: " + quadTree.totalObjects());
    }

}
